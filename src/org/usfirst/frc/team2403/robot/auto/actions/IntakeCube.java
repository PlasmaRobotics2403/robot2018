package org.usfirst.frc.team2403.robot.auto.actions;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.auto.util.Action;

/**
 *
 */
public class IntakeCube implements Action {

	boolean isIntaking;
	double speed;
	Intake intake;

	
	public IntakeCube(double speed, boolean isIntaking, Intake intake){
		this.isIntaking = isIntaking;
		this.speed = speed;
		this.intake = intake;
	}
	
	
	@Override
	public boolean isFinished() {
			return true;
	}
	
	@Override
	public void start() {
		if(isIntaking) {
			intake.in(speed);
		}
		else {
			intake.out(speed);
		}
	}

	@Override
	public void update() {

	}
	
	@Override
	public void end() {
	}

}
