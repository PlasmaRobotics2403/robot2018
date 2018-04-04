
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.Constants;
import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.Elevator;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.auto.actions.Clamp;
import org.usfirst.frc.team2403.robot.auto.actions.FollowTrajectory;
import org.usfirst.frc.team2403.robot.auto.actions.IntakeCube;
import org.usfirst.frc.team2403.robot.auto.actions.MoveLift;
import org.usfirst.frc.team2403.robot.auto.actions.PivotAngle;
import org.usfirst.frc.team2403.robot.auto.actions.Wait;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;

public class LeftScale extends AutoMode{ 

	DriveTrain drive;
	Intake intake;
	Elevator elevator;
	
	String gameData;
	
	public LeftScale(DriveTrain drive, Intake intake, Elevator elevator) {
		this.drive = drive;
		this.intake = intake;
		this.elevator = elevator;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}

	
	@Override
	protected void routine() throws AutoModeEndedException {
		DriverStation.reportWarning("running scale", false);
		if(gameData.charAt(0) == 'L') {
			runAction(new Clamp(intake, true));
			runAction(new FollowTrajectory("LeftScaleLeft1", drive));
			runActionsParallel(new FollowTrajectory("45 right", drive), new PivotAngle(Constants.PIVOT_POSITION_SCALE, elevator));
			runActionsParallel(new FollowTrajectory("LeftScaleLeft2", drive), new MoveLift(21, elevator));
			runAction(new IntakeCube(1, false, intake));
			runAction(new Wait(1));
			runAction(new IntakeCube(0, true, intake));
		}
		
		else {
			runAction(new FollowTrajectory("CrossBaseline", drive));
		}
	}
}