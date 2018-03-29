package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.Elevator;
import org.usfirst.frc.team2403.robot.auto.util.Action;

public class PivotAngle implements Action {
	
	int angle;
	Elevator elevator;
	
	public PivotAngle(int angle, Elevator elevator) {
		this.angle = angle;
		this.elevator = elevator;
	}

	@Override
	public boolean isFinished() {
		return elevator.getPivotLock();
	}

	@Override
	public void start() {
		elevator.setPivotTarget(angle);

	}

	@Override
	public void update() {
		elevator.pivotUpdate();

	}

	@Override
	public void end() {

	}

}
