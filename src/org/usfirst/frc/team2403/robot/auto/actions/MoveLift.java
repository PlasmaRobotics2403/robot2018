package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.Elevator;
import org.usfirst.frc.team2403.robot.auto.util.Action;

public class MoveLift implements Action {
	
	Elevator elevator;
	double position;
	
	public MoveLift(double position, Elevator elevator) {
		this.position = position;
		this.elevator = elevator;
	}

	@Override
	public boolean isFinished() {
		if(position == 0) {
			return elevator.getLiftDistance() == 0;
		}
		else {
			return Math.abs(position - elevator.getLiftDistance()) < .5;
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		if(position > elevator.getLiftDistance()) {
			elevator.elevatorLift(.8);
		}
		else {
			elevator.elevatorLift(-.8);
		}
	}

	@Override
	public void end() {
		elevator.elevatorLift(0);
	}

}
