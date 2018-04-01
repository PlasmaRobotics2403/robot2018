package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.Constants;
import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.Elevator;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.auto.actions.*;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;


public class TrajectoryTest extends AutoMode {
	
	DriveTrain drive;
	Elevator elevator;
	Intake intake;
	
	public TrajectoryTest(DriveTrain drive, Elevator elevator, Intake intake) {
		this.drive = drive;
		this.elevator = elevator;
		this.intake = intake;
	}

	@Override
	protected void routine() throws AutoModeEndedException {
	
		//runAction(new FollowTrajectory("left", drive));	
		runAction(new FollowTrajectory("allRight", drive));	
		/*
		runAction(new MoveLift(10, elevator));
		runAction(new FollowTrajectory("Straight", drive));
		runAction(new MoveLift(0, elevator));
		runAction(new PivotAngle(Constants.PIVOT_POSITION_BOTTOM, elevator));
		*/
		
		/*
		runAction(new Clamp(intake, false));
		runAction(new IntakeCube(1, true, intake));
		runAction(new FollowTrajectory("Spline", drive));
		runAction(new Clamp(intake, true));
		runAction(new IntakeCube(0, true, intake));
		runAction(new PivotAngle(Constants.PIVOT_POSITION_SWITCH, elevator));
		runAction(new FollowTrajectory("SplineR", drive));
		runAction(new IntakeCube(1, false, intake));
		runAction(new PivotAngle(Constants.PIVOT_POSITION_BOTTOM, elevator));
		runAction(new IntakeCube(0, true, intake));
		*/
	}

}
