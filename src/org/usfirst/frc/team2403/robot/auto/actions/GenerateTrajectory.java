package org.usfirst.frc.team2403.robot.auto.actions;

import java.io.File;

import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.DriverStation;
import jaci.pathfinder.*;
import jaci.pathfinder.modifiers.TankModifier;

public class GenerateTrajectory implements Action {
	
	boolean finished;
	
	public GenerateTrajectory() {
		finished = false;	
	}
	
	@Override
	public boolean isFinished() {
		
		return finished;
	}

	@Override
	public void start() {
		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, .01, 96, 24, 48);
		
		Waypoint[] points = new Waypoint[] {
				new Waypoint(0, 0, 0),
				new Waypoint(100, 0, 0)//,
				//new Waypoint(100, -50, Pathfinder.d2r(-90))
		};
		
		DriverStation.reportError("Starting Generation", true);
		Trajectory trajectory = Pathfinder.generate(points, config);
		
		TankModifier modifier = new TankModifier(trajectory).modify(26);
		
		Trajectory outside = modifier.getLeftTrajectory();
		Trajectory inside = modifier.getRightTrajectory();
		
		File outsideFile = new File("/home/lvuser/outside");
		File insideFile = new File("/home/lvuser/inside");
		
		Pathfinder.writeToFile(outsideFile, outside);
		Pathfinder.writeToFile(insideFile, inside);
		
		DriverStation.reportError("Generation finished", true);
		
		finished = true;
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
