package org.usfirst.frc.team2403.robot.auto.actions;

import java.io.File;

import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.DriverStation;
import jaci.pathfinder.*;
import jaci.pathfinder.modifiers.TankModifier;

public class GenerateTrajectory implements Action {
	
	boolean finished;
	boolean isReversed;
	Waypoint waypoints[];
	String name;
	
	public GenerateTrajectory(Waypoint waypoints[], String name, boolean isReversed) {
		finished = false;
		this.isReversed = isReversed;
		this.waypoints = waypoints;
		this.name = name;
		
	}
	
	@Override
	public boolean isFinished() {
		
		return finished;
	}

	@Override
	public void start() {
		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_HIGH, .01, 100, 50, 80);
		
		DriverStation.reportWarning("Starting Generation of Path: " + name, false);
		Trajectory trajectory = Pathfinder.generate(waypoints, config);
		TankModifier modifier = new TankModifier(trajectory).modify(26);
		
		Trajectory left;
		Trajectory right;
		
		if(!isReversed) {
			left = modifier.getLeftTrajectory();
			right = modifier.getRightTrajectory();
		}
		else {
			right = modifier.getLeftTrajectory();
			left = modifier.getRightTrajectory();
			for(int i = 0; i < right.segments.length; i++) {
				right.segments[i].position *= -1;
				right.segments[i].velocity *= -1;
			}
			for(int i = 0; i < left.segments.length; i++) {
				left.segments[i].position *= -1;
				left.segments[i].velocity *= -1;
			}
		}
		
		File leftFile = new File("/home/lvuser/" + name + "Left");
		File rightFile = new File("/home/lvuser/" + name + "Right");
		
		Pathfinder.writeToFile(leftFile, left);
		Pathfinder.writeToFile(rightFile, right);
		
		DriverStation.reportWarning("Ended Generation of Path: " + name, true);
		
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
