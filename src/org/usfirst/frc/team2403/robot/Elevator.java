package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.controllers.PlasmaTrigger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
	private TalonSRX leftPivot;
	private TalonSRX rightPivot;
	private TalonSRX leftLift;
	private TalonSRX rightLift;	
	
	private DoubleSolenoid pivotBrake;
	private DoubleSolenoid liftBrake;
	private Solenoid pivotHelper;
	
	private DigitalInput pivotLimit;
	private DigitalInput liftLimit;
	
	private double pivotSpeed;
	private double liftSpeed;
		
	public Elevator(int leftPivotID,int rightPivotID, int leftLiftID, int rightLiftID, int pivotLimID, int liftLimID) {
		
		pivotBrake = new DoubleSolenoid(Constants.PIVOT_OUT, Constants.PIVOT_IN);
		liftBrake = new DoubleSolenoid(Constants.LIFT_OUT, Constants.LIFT_IN);
		pivotHelper = new Solenoid(Constants.ELEVATOR);
		
		leftLift = new TalonSRX(leftLiftID);
		rightLift = new TalonSRX(rightLiftID);
		
		leftPivot = new TalonSRX(leftPivotID);
		rightPivot = new TalonSRX(rightPivotID);
		
		pivotLimit = new DigitalInput(pivotLimID);
		liftLimit = new DigitalInput(liftLimID);
		
		rightLift.set(ControlMode.PercentOutput, 0);
		leftLift.set(ControlMode.Follower, rightLift.getDeviceID());
		
		rightPivot.set(ControlMode.PercentOutput, 0);
		leftPivot.set(ControlMode.Follower, rightPivot.getDeviceID());
		
		leftPivot.setInverted(true);
		
		rightPivot.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightLift.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightLift.setSensorPhase(true);
		rightPivot.setSensorPhase(true);
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
		
		if(speed <= 0 && !pivotLimit.get()) {
			pivotSpeed = 0;
			rightPivot.setSelectedSensorPosition(0, 0, 0);
			activatePivotBrake();
		}
		else if(speed < 0 && getLiftLimit() + 1 < getLiftDistance()) {
			pivotSpeed = 0;
			activatePivotBrake();
		}
		else if(speed > 0) {
			releasePivotBrake();
			if(pivotSpeed < speed) {
				pivotSpeed += Constants.PIVOT_RAMP_RATE;
			}
		}
		else if(speed < 0) {
			releasePivotBrake();
			if(pivotSpeed > speed) {
				pivotSpeed -= Constants.PIVOT_RAMP_RATE;
			}
		}
		else {
			activatePivotBrake();
			pivotSpeed = 0;
		}
				
		rightPivot.set(ControlMode.PercentOutput, pivotSpeed);

	}
	
	public void reportPivotData() {
		SmartDashboard.putNumber("Pivot enc", rightPivot.getSelectedSensorPosition(0) * Constants.PIVOT_ENCODER_CONVERSION);
	}
	
	
	public double getPivotAngle() {
		return rightPivot.getSelectedSensorPosition(0) * Constants.PIVOT_ENCODER_CONVERSION;
	}
	
	public double getLiftDistance() {
		return rightLift.getSelectedSensorPosition(0) * Constants.LIFT_ENCODER_CONVERSION;
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
			return 27;
		}
		
	}
	
	public void elevatorLift(double speed) {
		speed *= Constants.MAX_LIFT_SPEED;
		
		if(speed < 0 && !liftLimit.get()) {
			liftSpeed = 0;
			activateLiftBrake();
			rightLift.setSelectedSensorPosition(0, 0, 0);
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
		
		
				
		rightLift.set(ControlMode.PercentOutput, liftSpeed);
		SmartDashboard.putNumber("Lift enc", rightLift.getSelectedSensorPosition(0) * Constants.LIFT_ENCODER_CONVERSION);
		
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
		liftBrake.set(DoubleSolenoid.Value.kForward);
	}
	
	public void releaseLiftBrake() {
		liftBrake.set(DoubleSolenoid.Value.kReverse);
	}
}

