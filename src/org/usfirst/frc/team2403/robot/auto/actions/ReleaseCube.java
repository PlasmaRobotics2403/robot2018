package org.usfirst.frc.team2403.robot.auto.actions;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
/**
 *
 */
public class ReleaseCube implements Action {

	double speed;
	Intake intake;
	
	public ReleaseCube(double speed, Intake intake){
		this.speed = speed;
		this.intake = intake;
	}
	
	@Override
	public boolean isFinished() {
		SmartDashboard.putNumber("Release Cube", 1);
		return false;
	}
	
	@Override
	public void start() {
			intake.spin(speed);
	}

	@Override
	public void update() {

	}
	
	@Override
	public void end() {
		intake.spin(0);
	}

}
