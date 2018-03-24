package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.auto.actions.GenerateTrajectory;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;

public class GenerateTrajectories extends AutoMode {
	
	public GenerateTrajectories() {
		
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		DriverStation.reportError("Starting generation...", false);
		runAction(new GenerateTrajectory());
		DriverStation.reportError("Generation ended", false);
	}

}
