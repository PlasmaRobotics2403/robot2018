package org.usfirst.frc.team2403.robot.auto.modes;

import java.io.File;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.auto.actions.*;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;

public class TrajectoryTest extends AutoMode {
	
	DriveTrain drive;
	
	public TrajectoryTest(DriveTrain drive) {
		this.drive = drive;
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		File outsideFile = new File("/home/lvuser/outside");
		File insideFile = new File("/home/lvuser/inside");
		
		Trajectory outside = Pathfinder.readFromFile(outsideFile);
		Trajectory inside = Pathfinder.readFromFile(insideFile);
		DriverStation.reportError("Starting follow", true);
		runAction(new FollowTrajectory(outside, inside, drive));
		DriverStation.reportError("Ending follow", true);

	}

}
