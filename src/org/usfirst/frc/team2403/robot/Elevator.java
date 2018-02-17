package org.usfirst.frc.team2403.robot;

// import edu.wpi.first.wpilibj.command.PIDSubsystem; o0
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import org.usfirst.frc.team2403.robot.controllers.PlasmaTrigger;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;




public class Elevator {
	
	private TalonSRX leftPivot;
	private TalonSRX rightPivot;
	private TalonSRX leftLift;
	private TalonSRX rightLift;
	private DoubleSolenoid pivotBrake;
	private DoubleSolenoid liftBrake;
	
	private int timer;
	
	public Elevator(int leftPivotID,int rightPivotID, int leftLiftID, int rightLiftID) {
		
		pivotBrake = new DoubleSolenoid(3,4);
		liftBrake = new DoubleSolenoid(5,6);
		
		leftLift = new TalonSRX(leftLiftID);
		rightLift = new TalonSRX(rightLiftID);
		
		leftPivot = new TalonSRX(leftPivotID);
		rightPivot = new TalonSRX(rightPivotID);
		
		leftLift.set(ControlMode.Follower, 0);
		rightLift.set(ControlMode.Position, 0);
		
		leftPivot.set(ControlMode.Position, 0);
		rightPivot.set(ControlMode.Position, 0);
		
		rightPivot.setInverted(true);
		
		leftPivot.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 0);
		//leftPivot.setSelectedSensorPosition(0, 0, 0);
		
		timer = 0;
		
	}

	public void PivotRotate(PlasmaTrigger raiseTrigger, PlasmaTrigger lowerTrigger) {
		
		double raiseVal = raiseTrigger.getTrueAxis();
		double lowerVal = lowerTrigger.getTrueAxis();
		
		PivotRotate(raiseVal, lowerVal);
	}
		
	public void PivotRotate(double lowerVal, double raiseVal) {
		
		lowerVal *= Constants.MAX_PIVOT_SPEED;
		raiseVal *= -Constants.MAX_PIVOT_SPEED;
		
		double speed;
		
		if(lowerVal > 0) {
			releasePivotBrake();
			while(timer < 10) {
				timer++;
			}
			speed = lowerVal;
		}
		else if(raiseVal < 0) {
			releasePivotBrake();
			while(timer < 10) {
				timer++;
			}
			speed = raiseVal;
		}
		else {
			activatePivotBrake();
			speed = 0;
			timer = 0;
		}
		
		if(leftPivot.getSelectedSensorPosition(0) > -30 || leftPivot.getSelectedSensorPosition(0) < 3000 ) {
			//activatePivotBrake();
		}
		
		speed *= Constants.MAX_PIVOT_SPEED;
		
		leftPivot.set(ControlMode.PercentOutput, speed);

	}
	
	public void reportPivotData() {
		SmartDashboard.putNumber("encoder", rightPivot.getSelectedSensorPosition(0));
	}
	
	public void elevatorLift(double speed) {
		rightLift.set(ControlMode.PercentOutput, speed * Constants.MAX_LIFT_SPEED);
		leftLift.set(ControlMode.PercentOutput, speed * Constants.MAX_LIFT_SPEED);
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
