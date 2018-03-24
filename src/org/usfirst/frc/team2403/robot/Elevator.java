package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.controllers.PlasmaTrigger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
	private TalonSRX leftPivot;
	private TalonSRX rightPivot;
	private TalonSRX leftLift;
	private TalonSRX rightLift;	
	
	private DoubleSolenoid pivotBrake;
	private DoubleSolenoid liftBrake;
	private DoubleSolenoid pivotHelper;
	
	private DigitalInput pivotLimit;
	private DigitalInput liftLimit;
	
	private double pivotSpeed;
	private double liftSpeed;
	
	private double pivotTarget;
	private boolean pivotLock;
 		
	public Elevator(int leftPivotID,int rightPivotID, int leftLiftID, int rightLiftID, int pivotLimID, int liftLimID) {
		
		pivotBrake = new DoubleSolenoid(Constants.PIVOT_OUT, Constants.PIVOT_IN);
		liftBrake = new DoubleSolenoid(Constants.LIFT_OUT, Constants.LIFT_IN);
		pivotHelper = new DoubleSolenoid(Constants.HELP_EXTEND, Constants.HELP_RETRACT);
		
		leftLift = new TalonSRX(leftLiftID);
		rightLift = new TalonSRX(rightLiftID);
		
		leftPivot = new TalonSRX(leftPivotID);
		rightPivot = new TalonSRX(rightPivotID);
		
		pivotLimit = new DigitalInput(pivotLimID);
		liftLimit = new DigitalInput(liftLimID);
		
		leftLift.set(ControlMode.PercentOutput, 0);
		rightLift.set(ControlMode.Follower, leftLift.getDeviceID());
		
		leftPivot.set(ControlMode.PercentOutput, 0);
		rightPivot.set(ControlMode.Follower, leftPivot.getDeviceID());
		
		limitCurrent(leftPivot);
		limitCurrent(rightPivot);
		
		rightPivot.setInverted(true);
		
		leftPivot.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightPivot.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		leftLift.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		
		leftPivot.setSensorPhase(true);
		rightPivot.setSensorPhase(true);
		leftLift.setSensorPhase(true);
		pivotSpeed = 0;
		liftSpeed = 0;
		
		pivotTarget = 0;
		pivotLock = false;
		
		/*change to leftPivot for Competition*/
		rightPivot.selectProfileSlot(0, 0);
		rightPivot.config_kF(0, 3, Constants.TALON_TIMEOUT);
		rightPivot.config_kP(0, 1.5, Constants.TALON_TIMEOUT);
		rightPivot.config_kI(0, .002, Constants.TALON_TIMEOUT);
		rightPivot.config_kD(0, 50, Constants.TALON_TIMEOUT);
		rightPivot.config_IntegralZone(0, 0, Constants.TALON_TIMEOUT);
		
		rightPivot.configMotionCruiseVelocity(200, Constants.TALON_TIMEOUT);
		rightPivot.configMotionAcceleration(150, Constants.TALON_TIMEOUT);
	}
	

	public void pivotRotate(PlasmaTrigger raiseTrigger, PlasmaTrigger lowerTrigger) {
		
		double raiseVal = raiseTrigger.getTrueAxis();
		double lowerVal = -lowerTrigger.getTrueAxis();
		
		if(raiseVal > 0) {
			pivotRotate(raiseVal);
		}
		else if(lowerVal < 0) {
			pivotRotate(lowerVal);
		}
		else {
			pivotRotate(0);
		}
	}
		
	public void pivotRotate(double speed) {
		
		speed *= Constants.MAX_PIVOT_SPEED;
		
		if(speed >= 0 && !pivotLimit.get()) {
			pivotSpeed = 0;
			leftPivot.setSelectedSensorPosition(0, 0, 0);
			rightPivot.setSelectedSensorPosition(0, 0, 0);
			activatePivotBrake();
			disengagePivotHelper();
		}
		else if(speed > 0 && getLiftLimit(getPivotAngle()) + 1 < getLiftDistance()) {
			pivotSpeed = 0;
			activatePivotBrake();
		}
		else if(speed > 0) {
			releasePivotBrake();
			disengagePivotHelper();
			if(pivotSpeed < speed) {
				pivotSpeed += Constants.PIVOT_RAMP_RATE;
			}
		}
		else if(speed < 0) {
			releasePivotBrake();
			engagePivotHelper();
			if(pivotSpeed > speed) {
				pivotSpeed -= Constants.PIVOT_RAMP_RATE;
			}
		}
		else {
			activatePivotBrake();
			pivotSpeed = 0;
		}
		
		rightPivot.set(ControlMode.Follower, leftPivot.getDeviceID());
		leftPivot.set(ControlMode.PercentOutput, pivotSpeed);
		SmartDashboard.putNumber("pivot speed", pivotSpeed);
	}
	
	/*Change to leftPivot for Conpetition*/
	public void reportPivotData() {
		SmartDashboard.putNumber("Pivot angle", (leftPivot.getSelectedSensorPosition(0) + rightPivot.getSelectedSensorPosition(0)) * Constants.PIVOT_ENCODER_CONVERSION);
		SmartDashboard.putNumber("Encoder Position", rightPivot.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Encoder Velocity", rightPivot.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("pivot Error", rightPivot.getClosedLoopError(0));
		SmartDashboard.putNumber("Pivot Position", pivotTarget);
	}
	
	/*flip left and right pivots for competition*/
	public void pivotUpdate() {
		if(!pivotLimit.get()) {
			rightPivot.setSelectedSensorPosition(0, 0, 0);
			leftPivot.setSelectedSensorPosition(0, 0, 0);
		}
		if(pivotTarget == 0 && rightPivot.getSelectedSensorPosition(0) > -20 && pivotLimit.get()) {
			leftPivot.set(ControlMode.Follower, rightPivot.getDeviceID());
			rightPivot.set(ControlMode.PercentOutput, .2);
		}
		else if(pivotLock || Math.abs(rightPivot.getSelectedSensorPosition(0) - pivotTarget) < 20) {
			activatePivotBrake();
			pivotLock = true;
			rightPivot.set(ControlMode.PercentOutput, 0);
			leftPivot.set(ControlMode.PercentOutput, 0);
		}
		else {
			releasePivotBrake();
			leftPivot.set(ControlMode.Follower, rightPivot.getDeviceID());
			rightPivot.set(ControlMode.MotionMagic, pivotTarget);
		}
	}
	
	public void setPivotTarget(double newTarget) {
		
		if(newTarget <= rightPivot.getSelectedSensorPosition(0) || getLiftLimit(pivotPosToAngle(newTarget)) + .1 >= getLiftDistance()) {
			pivotTarget = newTarget;
			pivotLock = false;
		}
	}
	
	public double getPivotAngle() {
		return (leftPivot.getSelectedSensorPosition(0) + rightPivot.getSelectedSensorPosition(0))  * Constants.PIVOT_ENCODER_CONVERSION;
	}
	
	public double pivotPosToAngle(double pivotPos) {
		return pivotPos * Constants.PIVOT_ENCODER_CONVERSION;
	}
	
	public double getLiftDistance() {
		return leftLift.getSelectedSensorPosition(0) * Constants.LIFT_ENCODER_CONVERSION;
	}
	
	public double getLiftLimit(double pivotAngle) {
		if(pivotAngle < 40) {
			return 0;
		}
		else if(pivotAngle < 65) {
			return 10;
		}
		else if(pivotAngle < 75) {
			return 20;
		}
		else {
			return 28;
		}
		
	}
	
	public void elevatorLift(double speed) {
		speed *= Constants.MAX_LIFT_SPEED;
		
		if(speed < 0 && !liftLimit.get()) {
			liftSpeed = 0;
			activateLiftBrake();
			leftLift.setSelectedSensorPosition(0, 0, 0);
		}
		
		else if(speed > 0 && getLiftLimit(getPivotAngle()) + .5 < getLiftDistance()) {
			liftSpeed = 0;
			activateLiftBrake();
		}
		
		else if(speed > 0) {
			releaseLiftBrake();
			if(liftSpeed < speed) {
				liftSpeed += Constants.LIFT_RAMP_RATE;
			}
		}
		
		else if(speed < 0) {
			releaseLiftBrake();
			if(liftSpeed > speed) {
				liftSpeed -= Constants.LIFT_RAMP_RATE;
				
			}
		}
		
		else {
			activateLiftBrake();
			liftSpeed = 0;
		}
				
		leftLift.set(ControlMode.PercentOutput, liftSpeed);
		SmartDashboard.putNumber("Lift enc", leftLift.getSelectedSensorPosition(0) * Constants.LIFT_ENCODER_CONVERSION);
	}
	
	public void limitCurrent(TalonSRX talon) {
		talon.configPeakCurrentDuration(0, 1000);
		talon.configPeakCurrentLimit(25, 1000);
		talon.configContinuousCurrentLimit(25, 1000);
		talon.enableCurrentLimit(true);
	}
	
	public void extend(double speed) {
		elevatorLift(speed);
	}
	
	public void retract(double speed) {
		elevatorLift(-speed);
	}
	
	public void activatePivotBrake() {
		pivotBrake.set(DoubleSolenoid.Value.kForward);
	}
	
	public void releasePivotBrake() {
		pivotBrake.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void activateLiftBrake() {
		liftBrake.set(DoubleSolenoid.Value.kReverse);
	}
	
	
	public void releaseLiftBrake() {
		liftBrake.set(DoubleSolenoid.Value.kForward);
	}
	
	public void engagePivotHelper() {
		pivotHelper.set(DoubleSolenoid.Value.kForward);
	}
	
	public void disengagePivotHelper() {
		pivotHelper.set(DoubleSolenoid.Value.kReverse);
	}
}

