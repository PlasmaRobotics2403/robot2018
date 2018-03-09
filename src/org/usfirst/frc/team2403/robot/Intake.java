package org.usfirst.frc.team2403.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Intake {

	private TalonSRX leftIntake;
	private TalonSRX rightIntake;
	private DoubleSolenoid clamp;
	
	public Intake(int intakeLeftID, int intakeRightID) {
		
		leftIntake = new TalonSRX(intakeLeftID);
		rightIntake = new TalonSRX(intakeRightID);
		clamp = new DoubleSolenoid(Constants.CLAMP_OUT, Constants.CLAMP_IN);
		
		leftIntake.setInverted(true);
	}
	
	
	public void spin(double speed) {
		leftIntake.set(ControlMode.Current, .5);
		rightIntake.set(ControlMode.Current, .5);
		leftIntake.set(ControlMode.PercentOutput, speed * Constants.MAX_INTAKE_SPEED);
		rightIntake.set(ControlMode.PercentOutput, speed * Constants.MAX_INTAKE_SPEED);
	}
	
	public void in(double speed) {
		spin(speed);
	}
	
	public void out(double speed) {
		spin(-speed);
	}
	
	public void spinRight(double speed) {
		rightIntake.set(ControlMode.PercentOutput, speed * Constants.MAX_INTAKE_SPEED);
	}
	
	public void spinLeft(double speed) {
		leftIntake.set(ControlMode.PercentOutput, speed * Constants.MAX_INTAKE_SPEED);
	}
	
	public void release() {
		clamp.set(DoubleSolenoid.Value.kReverse);

	}
	public void clamp() {
		clamp.set(DoubleSolenoid.Value.kForward);

	}
}
