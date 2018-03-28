package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.controllers.PlasmaAxis;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class DriveTrain {
	
	public TalonSRX leftDrive;
	public TalonSRX leftDriveSlave;
	public TalonSRX rightDrive;
	public TalonSRX rightDriveSlave;
	
	private AHRS navX;
	private double gyroAngle;
	
	public DriveTrain(int leftID, int leftSID, int rightID, int rightSID) {

		leftDrive = new TalonSRX(leftID);
		leftDriveSlave = new TalonSRX(leftSID);
		rightDrive = new TalonSRX(rightID);
		rightDriveSlave = new TalonSRX(rightSID);
		
		leftDrive.set(ControlMode.Position, 0);
		rightDrive.set(ControlMode.Position, 0);		
		leftDriveSlave.set(ControlMode.Position, 0);
		rightDriveSlave.set(ControlMode.Position, 0);
		
		limitCurrent(leftDrive);
		limitCurrent(rightDrive);
		limitCurrent(leftDriveSlave);
		limitCurrent(rightDriveSlave);
		
		leftDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightDrive.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		leftDrive.setSensorPhase(true);
		rightDrive.setSensorPhase(true);
		
		leftDrive.setInverted(false);
		leftDriveSlave.setInverted(false);
		rightDrive.setInverted(true);
		rightDriveSlave.setInverted(true);
		
		navX = new AHRS(SPI.Port.kMXP);
		
		leftDrive.selectProfileSlot(0, 0);
		leftDrive.config_kF(0, .35, Constants.TALON_TIMEOUT);
		leftDrive.config_kP(0, 1.2, Constants.TALON_TIMEOUT);
		leftDrive.config_kI(0, .005, Constants.TALON_TIMEOUT);
		leftDrive.config_kD(0, 25, Constants.TALON_TIMEOUT);
		leftDrive.config_IntegralZone(0, 0, Constants.TALON_TIMEOUT);
		
		rightDrive.selectProfileSlot(0, 0);
		rightDrive.config_kF(0, .35, Constants.TALON_TIMEOUT);
		rightDrive.config_kP(0, 1.2, Constants.TALON_TIMEOUT);
		rightDrive.config_kI(0, .005, Constants.TALON_TIMEOUT);
		rightDrive.config_kD(0, 25, Constants.TALON_TIMEOUT);
		rightDrive.config_IntegralZone(0, 0, Constants.TALON_TIMEOUT);
		
		leftDrive.setControlFramePeriod(ControlFrame.Control_3_General, 5);
		rightDrive.setControlFramePeriod(ControlFrame.Control_3_General, 5);
		leftDriveSlave.setControlFramePeriod(ControlFrame.Control_3_General, 5);
		rightDriveSlave.setControlFramePeriod(ControlFrame.Control_3_General, 5);
	}
	
	public void resetEncoders(){
		double dist = Math.abs(getDistance());
		leftDrive.set(ControlMode.Position, 0);
		rightDrive.set(ControlMode.Position, 0);
		while(Math.abs(getDistance()) > dist - 1 && Math.abs(getDistance()) > 1){
			leftDrive.setSelectedSensorPosition(0, 0, 0);
			rightDrive.setSelectedSensorPosition(0, 0, 0);
		}
	}
	
	public double getDistance(){
		SmartDashboard.putNumber("distL", toDistance(leftDrive));
		SmartDashboard.putNumber("distR", toDistance(rightDrive));
		SmartDashboard.putNumber("velL", leftDrive.getSelectedSensorVelocity(0) * Constants.DRIVE_ENCODER_CONVERSION / .1);
		SmartDashboard.putNumber("velR", rightDrive.getSelectedSensorVelocity(0) * Constants.DRIVE_ENCODER_CONVERSION / .1);
		return (toDistance(rightDrive) + toDistance(leftDrive))/2;
	}
	
	
	public double getLeftDistance(){
		return toDistance(leftDrive);
	}
	

	private static double toDistance(TalonSRX talon){
		return talon.getSelectedSensorPosition(0) * Constants.DRIVE_ENCODER_CONVERSION;
	}
	
	public void updateGyro(){
		gyroAngle = navX.getYaw();
	}
	
	public double getGyroAngle(){
		updateGyro();
		return gyroAngle;
	}
	
	public void zeroGyro(){
		navX.zeroYaw();
	}	
	
	public void FPSDrive(PlasmaAxis forwardAxis, PlasmaAxis turnAxis){
		
		double forwardVal = forwardAxis.getFilteredAxis() * Math.abs(forwardAxis.getFilteredAxis());
		double turnVal = turnAxis.getFilteredAxis() * Math.abs(turnAxis.getFilteredAxis()) * Math.abs(turnAxis.getFilteredAxis());
		
		FPSDrive(forwardVal, turnVal);
	}
	
	public void FPSDrive(double forwardVal, double turnVal){
		
		turnVal *= Constants.MAX_DRIVE_TURN;
		
		double absForward = Math.abs(forwardVal);
		double absTurn = Math.abs(turnVal);
		
		int forwardSign = (forwardVal == 0) ? 0 : (int)(forwardVal / Math.abs(forwardVal));
		int turnSign = (turnVal == 0) ? 0 : (int)(turnVal / Math.abs(turnVal));
		
		double speedL;
		double speedR;
		
		if(turnVal == 0){ //Straight forward
			speedL = forwardVal;
			speedR = forwardVal;
		}
		else if(forwardVal == 0){ //Pivot turn
			speedL = turnVal;
			speedR = -turnVal;
		}
		else if(forwardSign == 1 && turnSign == 1){ //Forward right
			speedL = forwardVal;
			speedR = (absForward - absTurn < 0) ? 0 : absForward - absTurn;
		}
		else if(forwardSign == 1 && turnSign == -1){ //Forward left
			speedL = (absForward - absTurn < 0) ? 0 : absForward - absTurn;
			speedR = forwardVal;
		}
		else if(forwardSign == -1 && turnSign == -1){ //Backward right
			speedL = (absForward - absTurn < 0) ? 0 : -(absForward - absTurn);
			speedR = forwardVal;
		}
		else if(forwardSign == -1 && turnSign == 1){ //Backward left
			speedL = forwardVal;
			speedR = (absForward - absTurn < 0) ? 0 : -(absForward - absTurn);
		}
		else{
			speedL = 0;
			speedR = 0;
			DriverStation.reportError("Bug @ fps drive code - no case triggered)", false);
		}
		
		speedL *= Constants.MAX_DRIVE_SPEED;
		speedR *= Constants.MAX_DRIVE_SPEED;
		
		leftDrive.set(ControlMode.PercentOutput, speedL);
		rightDrive.set(ControlMode.PercentOutput, speedR);
		leftDriveSlave.set(ControlMode.PercentOutput,speedL);
		rightDriveSlave.set(ControlMode.PercentOutput, speedR);
		
		SmartDashboard.putNumber("drive enc vel", leftDrive.getSelectedSensorVelocity(0) * 10 * Constants.DRIVE_ENCODER_CONVERSION);
		SmartDashboard.putNumber("drive power", speedL);
		
	}
	
	public void limitCurrent(TalonSRX talon) {
		talon.configPeakCurrentDuration(0, 1000);
		talon.configPeakCurrentLimit(25, 1000);
		talon.configContinuousCurrentLimit(25, 1000);
		talon.enableCurrentLimit(true);
	}
	
	public void autonTankDrive(double left, double right){
		leftWheelDrive(left);
		rightWheelDrive(right);
	}
	
	public void leftWheelDrive(double speed){
		leftDrive.set(ControlMode.PercentOutput ,speed * Constants.MAX_AUTO_DRIVE_SPEED);
		leftDriveSlave.set(ControlMode.PercentOutput ,speed * Constants.MAX_AUTO_DRIVE_SPEED);
	}
	
	public void rightWheelDrive(double speed){
		rightDrive.set(ControlMode.PercentOutput ,speed * Constants.MAX_AUTO_DRIVE_SPEED);
		rightDriveSlave.set(ControlMode.PercentOutput ,speed * Constants.MAX_AUTO_DRIVE_SPEED);
	}
	
	public void gyroStraight(double speed, double angle){
		if(getGyroAngle() > 0) {
			autonTankDrive(speed - 0.01*(getGyroAngle() - angle), speed - 0.01*(getGyroAngle() - angle));
		}
		else if(getGyroAngle() < 0) {
			autonTankDrive(speed - 0.01*(getGyroAngle() + angle), speed - 0.01*(getGyroAngle() + angle));
		}
		else {
			autonTankDrive(speed - 0.01*(getGyroAngle() + angle), speed - 0.01*(getGyroAngle()+ angle));
		}
	}
	
	public void pivotToAngle(double angle){
		double angleDiff = getGyroAngle() - angle;
		double speed = (Math.abs(angleDiff) < 10) ? (Math.abs(angleDiff) / 10.0) * 0.15 + 0.15 : .3;
		if(angleDiff > 0){
			autonTankDrive(-speed, speed);
		}
		else{
			autonTankDrive(speed, -speed);
		}
	}
	
	public void stopDrive(){
		autonTankDrive(0, 0);
	}
}
