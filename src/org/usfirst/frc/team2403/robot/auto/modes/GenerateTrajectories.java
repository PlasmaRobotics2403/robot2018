package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.auto.actions.GenerateTrajectory;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;
import jaci.pathfinder.Waypoint;

public class GenerateTrajectories extends AutoMode {
	
	public GenerateTrajectories() {
		
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		DriverStation.reportWarning("Starting generation...", false);
		
		Waypoint waypoints1[] = new Waypoint[] {
				new Waypoint(0, 0, 0),
				new Waypoint(70, 0, 0)
		};
		
		runAction(new GenerateTrajectory(waypoints1, "Straight" , false));
		runAction(new GenerateTrajectory(waypoints1, "StraightR" , true));
		
		Waypoint waypoints2[] = new Waypoint[] {
				
				new Waypoint(0,0,0),
				new Waypoint(70, -50, 0),
		};
		
		runAction(new GenerateTrajectory(waypoints2, "Spline", false));
		runAction(new GenerateTrajectory(waypoints2, "SplineR", true));
		DriverStation.reportWarning("All Generation ended", false);
	}

}
