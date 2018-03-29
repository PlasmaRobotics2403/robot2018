package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.auto.util.Action;

public class Clamp implements Action {
	
	boolean isClamping;
	Intake intake;
	
	public Clamp(Intake intake, boolean isClamping) {
		this.intake = intake;
		this.isClamping = isClamping;
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void start() {
		if(isClamping) {
			intake.clamp();
		}
		else {
			intake.release();
		}

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void end() {
		// TODO Auto-generated method stub

	}

}
