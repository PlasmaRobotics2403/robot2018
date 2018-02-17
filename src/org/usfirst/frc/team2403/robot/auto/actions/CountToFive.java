/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 */
public class CountToFive implements Action {

	int lol;
	boolean test;
	
	public CountToFive(){
		
	}
	
	@Override
	public boolean isFinished() {
		return lol >= 5;
	}

	@Override
	public void start() {
		test = false;
		lol = 0;

	}

	@Override
	public void update() {
		lol++;
		DriverStation.reportWarning("" + lol, false);
	}

	@Override
	public void end() {

	}

}
