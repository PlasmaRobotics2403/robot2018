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
	int leftIndex;
	int rightIndex;
	
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
		leftIndex = 0;
		rightIndex = 0;
	}

	@Override
	public boolean isFinished() {
		MotionProfileStatus status = new MotionProfileStatus();
		drive.rightDrive.getMotionProfileStatus(status);
		return status.isLast;
	}
	
	private void bufferPoints() {
		for(int i = leftIndex; i < leftTrajectory.length(); i++) {
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
			if(drive.leftDrive.isMotionProfileTopLevelBufferFull()) {
				leftIndex = i;
				break;
			}
			else {
				drive.leftDrive.pushMotionProfileTrajectory(point);	
				leftIndex = i + 1;
			}
		}
		
		for(int i = rightIndex; i < rightTrajectory.length(); i++) {
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
			if(drive.rightDrive.isMotionProfileTopLevelBufferFull()) {
				rightIndex = i;
				break;
			}
			else {
				drive.rightDrive.pushMotionProfileTrajectory(point);	
				rightIndex = i + 1;
			}
		}
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
		
		bufferPoints();
		
		notifier.startPeriodic(.005);
	}

	@Override
	public void update() {
		bufferPoints();
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


