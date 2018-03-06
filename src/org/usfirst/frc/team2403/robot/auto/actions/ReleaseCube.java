package org.usfirst.frc.team2403.robot.auto.actions;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class ReleaseCube implements Action {

	double speed;
	Timer time;
	Intake intake;

	
	public ReleaseCube(double speed, Intake intake){
		this.speed = speed;
		this.intake = intake;
		time = new Timer();
	}
	
	
	@Override
	public boolean isFinished() {
		if(time.get() >= 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void start() {
		time.start();
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
