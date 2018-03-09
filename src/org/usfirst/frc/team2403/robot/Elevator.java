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
		leftLift.set(ControlMode.Current, .5);
		rightLift.set(ControlMode.Follower, leftLift.getDeviceID());
		
		leftPivot.set(ControlMode.PercentOutput, 0);
		leftPivot.set(ControlMode.Current, .5);
		rightPivot.set(ControlMode.Follower, leftPivot.getDeviceID());
		
		rightPivot.setInverted(true);
		
		leftPivot.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		leftLift.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		leftLift.setSensorPhase(true);
		leftPivot.setSensorPhase(true);
		pivotSpeed = 0;
		liftSpeed = 0;
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
			activatePivotBrake();
			disengagePivotHelper();
		}
		else if(speed < 0 && getLiftLimit() + 1 < getLiftDistance()) {
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
				
		leftPivot.set(ControlMode.PercentOutput, pivotSpeed);

	}
	
	public void reportPivotData() {
		SmartDashboard.putNumber("Pivot enc", leftPivot.getSelectedSensorPosition(0) * Constants.PIVOT_ENCODER_CONVERSION);
	}
	
	
	public double getPivotAngle() {
		return leftPivot.getSelectedSensorPosition(0) * Constants.PIVOT_ENCODER_CONVERSION;
	}
	
	public double getLiftDistance() {
		return leftLift.getSelectedSensorPosition(0) * Constants.LIFT_ENCODER_CONVERSION;
	}
	
	public double getLiftLimit() {
		if(getPivotAngle() < 65) {
			return 0;
		}
		else if(getPivotAngle() < 75) {
			return 10;
		}
		if(getPivotAngle() < 85) {
			return 20;
		}
		else {
			return 30;
		}
		
	}
	
	public void elevatorLift(double speed) {
		speed *= Constants.MAX_LIFT_SPEED;
		
		if(speed < 0 && !liftLimit.get()) {
			liftSpeed = 0;
			activateLiftBrake();
			leftLift.setSelectedSensorPosition(0, 0, 0);
		}
		
		else if(speed > 0 && getLiftDistance() > getLiftLimit()) {
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

