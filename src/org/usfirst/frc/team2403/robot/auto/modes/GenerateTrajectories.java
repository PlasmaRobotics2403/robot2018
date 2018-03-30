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
				new Waypoint(30, 0, 0)
		};
		
		Waypoint waypoints2[] = new Waypoint[] {
				
				new Waypoint(0,0,0),
				new Waypoint(108, -50, 0),
		};
		
		Waypoint waypoints3[] = new Waypoint[] {
				new Waypoint(0,0,0),
				new Waypoint(75, -50, 0),
		};
		
		runAction(new GenerateTrajectory(waypoints2, "SwitchCenterRight1", false));
		runAction(new GenerateTrajectory(waypoints3, "SwitchCenterRight2", true));
		runAction(new GenerateTrajectory(waypoints1, "SwitchCenterRight3" , false));
		runAction(new GenerateTrajectory(waypoints1, "SwitchCenterRight4" , true));
		runAction(new GenerateTrajectory(waypoints3, "SwitchCenterRight5", false));		
		
		Waypoint waypoints4[] = new Waypoint[] {
				new Waypoint(0, 0, 0),
				new Waypoint(30, 0, 0)
		};
		
		Waypoint waypoints5[] = new Waypoint[] {
				
				new Waypoint(0,0,0),
				new Waypoint(108, 50, 0),
		};
		
		Waypoint waypoints6[] = new Waypoint[] {
				new Waypoint(0,0,0),
				new Waypoint(75, 50, 0),
		};
		
		runAction(new GenerateTrajectory(waypoints5, "CenterSwitchLeft1", false));
		runAction(new GenerateTrajectory(waypoints6, "CenterSwitchLeft2", true));
		runAction(new GenerateTrajectory(waypoints4, "CenterSwitchLeft3" , false));
		runAction(new GenerateTrajectory(waypoints4, "CenterSwitchLeft4" , true));
		runAction(new GenerateTrajectory(waypoints6, "CenterSwitchLeft5", false));
		
		Waypoint waypoint7[] = new Waypoint[] {
				new Waypoint(0,0,0),
				new Waypoint(196, 0, 0),
				new Waypoint(288, -70, 0),
		};
		
		runAction(new GenerateTrajectory(waypoint7, "LeftScaleLeft1", false));
		
		DriverStation.reportWarning("All Generation ended", false);
	}

}
