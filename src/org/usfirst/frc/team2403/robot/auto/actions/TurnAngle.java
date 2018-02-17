/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 *
 */
public class TurnAngle implements Action {

	double speed;
	double angle;
	double angleDiff;
	
	double speedPValue;
	double minSpeed;
	double fullSpeedTime;
	
	double fullStartTime;
	
	DriveTrain drive;
	
	public TurnAngle(double speed, double angle, DriveTrain drive){
		if(angle >=0){
			this.speed = Math.abs(speed); //speed is default left
		}
		else{
			this.speed = -Math.abs(speed);
		}
		this.angle = angle;
		this.drive = drive;
		speedPValue = .005;
		minSpeed = .6 * this.speed / Math.abs(this.speed);
		fullSpeedTime = .12;
	}
	
	@Override
	public boolean isFinished() {
		SmartDashboard.putNumber("Turn Angle" , 1);
		return Math.abs(angleDiff) < 2;
	}

	@Override
	public void start() {
		drive.resetEncoders();
		while(Math.abs(drive.getDistance()) > 1){
			drive.resetEncoders();
		}
		drive.zeroGyro();
		angleDiff = angle;
		fullStartTime = Timer.getFPGATimestamp();
	}

	@Override
	public void update() {
		angleDiff = angle - drive.getGyroAngle();
		if(Timer.getFPGATimestamp() - fullStartTime < fullSpeedTime){
			drive.autonTankDrive(speed, -speed);
			DriverStation.reportWarning("forced full", false);
		}
		else if(Math.abs(angleDiff) * speedPValue < Math.abs(minSpeed)){ //Prop slowdown speeds would be slower than min speed
			drive.autonTankDrive(minSpeed, -minSpeed);
			DriverStation.reportWarning("min", false);
		}
		else if(Math.abs(angleDiff) * speedPValue < Math.abs(speed)){ //Prop slowdown speeds would be slower than max speed
			drive.autonTankDrive(speedPValue * angleDiff, speedPValue * -angleDiff);
			DriverStation.reportWarning("slow", false);
		}
		else{
			drive.autonTankDrive(speed, -speed);
			DriverStation.reportWarning("full", false);
		}
	}
	

	//@Override
	public void end() {
		drive.stopDrive();
		DriverStation.reportWarning("done", false);
	}

}
