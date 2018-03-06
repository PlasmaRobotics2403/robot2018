/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.auto.actions.DriveStraight;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

/**
 *
 */
public class CrossBaseline extends AutoMode {
	
	DriveTrain drive;
	
	public CrossBaseline(DriveTrain drive){
		this.drive = drive;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new DriveStraight(.4, 96, drive));
	}

}
