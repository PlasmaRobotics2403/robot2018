package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.Constants;
import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.*;
import jaci.pathfinder.followers.EncoderFollower;

public class FollowTrajectory implements Action {

	DriveTrain drive;
	Notifier followLoop;
	EncoderFollower leftFollower;
	EncoderFollower rightFollower;
	
	class PeriodicRunnable implements java.lang.Runnable{
		public void run() {
			double l = leftFollower.calculate(drive.leftDrive.getSelectedSensorPosition(0));
			double r = rightFollower.calculate(drive.rightDrive.getSelectedSensorPosition(0));
			
			double currentHeading = -drive.getGyroAngle();
			double desiredHeading = Pathfinder.r2d(leftFollower.getHeading());
			
			double angleDifference = Pathfinder.boundHalfDegrees(desiredHeading - currentHeading);
			double turn = 0 * angleDifference;
			
			SmartDashboard.putNumber("angleDiff", angleDifference);
			
			double speedL = (l - turn) * .1 / Constants.DRIVE_ENCODER_CONVERSION;
			double speedR = (r + turn) * .1 / Constants.DRIVE_ENCODER_CONVERSION;
			
			drive.leftDrive.set(ControlMode.Velocity, speedL);
			drive.rightDrive.set(ControlMode.Velocity, speedR);
			drive.leftDriveSlave.set(ControlMode.Follower, drive.leftDrive.getDeviceID());
			drive.rightDriveSlave.set(ControlMode.Follower, drive.rightDrive.getDeviceID());
		}
	}
	
	public FollowTrajectory(Trajectory leftTrajectory, Trajectory rightTrajectory, DriveTrain drive) {
		this.drive = drive;
		this.followLoop = new Notifier(new PeriodicRunnable());
		leftFollower = new EncoderFollower(leftTrajectory);
		rightFollower = new EncoderFollower(rightTrajectory);
	}

	@Override
	public boolean isFinished() {
		return leftFollower.isFinished();
	}
	
	@Override
	public void start() {
		drive.leftDrive.setSelectedSensorPosition(0, 0, 0);
		drive.rightDrive.setSelectedSensorPosition(0, 0, 0);
		drive.zeroGyro();
		
		leftFollower.configureEncoder(drive.leftDrive.getSelectedSensorPosition(0), (int) (1/Constants.DRIVE_ENCODER_CONVERSION), 1/Math.PI);
		rightFollower.configureEncoder(drive.rightDrive.getSelectedSensorPosition(0), (int) (1/Constants.DRIVE_ENCODER_CONVERSION), 1/Math.PI);
		leftFollower.configurePIDVA(.3, 0, 0, 1, 0);
		rightFollower.configurePIDVA(.3, 0, 0, 1, 0);
		followLoop.startPeriodic(.01);
		
		
	}

	@Override
	public void update() {
		SmartDashboard.putNumber("Left Error", drive.leftDrive.getClosedLoopError(0));
		SmartDashboard.putNumber("Right Error", drive.rightDrive.getClosedLoopError(0));
		SmartDashboard.putNumber("leftPosition Error", leftFollower.getSegment().position - (drive.leftDrive.getSelectedSensorPosition(0) * Constants.DRIVE_ENCODER_CONVERSION));
	}

	@Override
	public void end() {
		followLoop.stop();
		DriverStation.reportWarning("Loop Killed", false);
		drive.leftDrive.set(ControlMode.PercentOutput, 0);
		drive.rightDrive.set(ControlMode.PercentOutput, 0);
		drive.leftDriveSlave.set(ControlMode.PercentOutput, 0);
		drive.rightDriveSlave.set(ControlMode.PercentOutput, 0);
	}

}


