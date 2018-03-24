package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.Constants;
import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.*;

public class FollowTrajectory implements Action {
	
	Trajectory leftTrajectory;
	Trajectory rightTrajectory;
	DriveTrain drive;
	Notifier notifier;
	
	class PeriodicRunnable implements java.lang.Runnable{
		public void run() {
			drive.leftDrive.processMotionProfileBuffer();
			drive.rightDrive.processMotionProfileBuffer();
		}
	}
	
	public FollowTrajectory(Trajectory leftTrajectory, Trajectory rightTrajectory, DriveTrain drive) {
		this.leftTrajectory = leftTrajectory;
		this.rightTrajectory = rightTrajectory;
		this.drive = drive;
		this.notifier = new Notifier(new PeriodicRunnable());
	}

	@Override
	public boolean isFinished() {
		MotionProfileStatus status = new MotionProfileStatus();
		drive.rightDrive.getMotionProfileStatus(status);
		return status.isLast;
	}

	@Override
	public void start() {
		
		drive.leftDrive.clearMotionProfileTrajectories();
		drive.rightDrive.clearMotionProfileTrajectories();
		
		drive.leftDrive.clearMotionProfileHasUnderrun(0);
		drive.rightDrive.clearMotionProfileHasUnderrun(0);
		
		drive.leftDrive.configMotionProfileTrajectoryPeriod(0, Constants.TALON_TIMEOUT);
		drive.rightDrive.configMotionProfileTrajectoryPeriod(0, Constants.TALON_TIMEOUT);
		
		drive.leftDrive.set(ControlMode.MotionProfile, 0);
		drive.rightDrive.set(ControlMode.MotionProfile, 0);
		
		for(int i = 0; i < leftTrajectory.length(); i++) {
			TrajectoryPoint point = new TrajectoryPoint();
			double positionInch = leftTrajectory.get(i).position;
			double velocityInch = leftTrajectory.get(i).velocity;
			
			point.position = positionInch / Constants.DRIVE_ENCODER_CONVERSION;
			point.velocity = (velocityInch * .1) / Constants.DRIVE_ENCODER_CONVERSION;
			point.headingDeg = 0;
			point.profileSlotSelect0 = 0;
			point.profileSlotSelect1 = 0;
			point.timeDur = TrajectoryDuration.Trajectory_Duration_10ms;
			point.zeroPos = false;
			if(i == 0) {
				point.zeroPos = true;
			}
			point.isLastPoint = false;
			if(i + 1 == leftTrajectory.length()) {
				point.isLastPoint = true;
			}
			drive.leftDrive.pushMotionProfileTrajectory(point);
		}
		
		for(int i = 0; i < rightTrajectory.length(); i++) {
			TrajectoryPoint point = new TrajectoryPoint();
			double positionInch = rightTrajectory.get(i).position;
			double velocityInch = rightTrajectory.get(i).velocity;
			
			point.position = (positionInch) / Constants.DRIVE_ENCODER_CONVERSION;
			point.velocity = (velocityInch * .1) / Constants.DRIVE_ENCODER_CONVERSION;
			point.headingDeg = 0;
			point.profileSlotSelect0 = 0;
			point.profileSlotSelect1 = 0;
			point.timeDur = TrajectoryDuration.Trajectory_Duration_10ms;
			point.zeroPos = false;
			if(i == 0) {
				point.zeroPos = true;
			}
			point.isLastPoint = false;
			if(i + 1 == rightTrajectory.length()) {
				point.isLastPoint = true;
			}
			drive.rightDrive.pushMotionProfileTrajectory(point);
		}
		notifier.startPeriodic(.005);
	}

	@Override
	public void update() {
		MotionProfileStatus status = new MotionProfileStatus();
		drive.leftDrive.getMotionProfileStatus(status);
		if(status.btmBufferCnt > 64) {
			drive.leftDrive.set(ControlMode.MotionProfile, 1);
			drive.rightDrive.set(ControlMode.MotionProfile, 1);
			drive.leftDriveSlave.set(ControlMode.Follower, drive.leftDrive.getDeviceID());
			drive.rightDriveSlave.set(ControlMode.Follower, drive.rightDrive.getDeviceID());
		}
		SmartDashboard.putNumber("Left Error", drive.leftDrive.getClosedLoopError(0));
		SmartDashboard.putNumber("Right Error", drive.rightDrive.getClosedLoopError(0));
	}

	@Override
	public void end() {
		notifier.stop();
		drive.leftDrive.set(ControlMode.PercentOutput, 0);
		drive.rightDrive.set(ControlMode.PercentOutput, 0);
		drive.leftDriveSlave.set(ControlMode.PercentOutput, 0);
		drive.rightDriveSlave.set(ControlMode.PercentOutput, 0);
	}

}


