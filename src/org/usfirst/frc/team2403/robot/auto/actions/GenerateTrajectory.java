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
		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, .01, 100, 50, 80);
		
		Waypoint[] points = new Waypoint[] {
				new Waypoint(0, 0, 0),
				new Waypoint(60, -40, 0),
				new Waypoint(80, -40, 0),
				new Waypoint(140, 0, 0)
		};
		
		DriverStation.reportError("Starting Generation", true);
		Trajectory trajectory = Pathfinder.generate(points, config);
		
		TankModifier modifier = new TankModifier(trajectory).modify(27);
		
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
