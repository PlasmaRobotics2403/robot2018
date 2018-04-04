
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.Constants;
import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.Elevator;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.auto.actions.Clamp;
import org.usfirst.frc.team2403.robot.auto.actions.FollowTrajectory;
import org.usfirst.frc.team2403.robot.auto.actions.IntakeCube;
import org.usfirst.frc.team2403.robot.auto.actions.PivotAngle;
import org.usfirst.frc.team2403.robot.auto.actions.Wait;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;

public class CenterSwitch extends AutoMode{ 

	DriveTrain drive;
	Intake intake;
	Elevator elevator;
	
	String gameData;
	
	public CenterSwitch(DriveTrain drive, Intake intake, Elevator elevator) {
		this.drive = drive;
		this.intake = intake;
		this.elevator = elevator;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}

	
	@Override
	protected void routine() throws AutoModeEndedException {		
		if(gameData.charAt(0) == 'R') {
			runAction(new Clamp(intake, true));
			runAction(new FollowTrajectory("CenterSwitchRight1", drive));
			runAction(new IntakeCube(.7, false, intake));
			runActionsParallel(new PivotAngle(Constants.PIVOT_POSITION_BOTTOM, elevator), new FollowTrajectory("CenterSwitchRight2", drive));
			runAction(new IntakeCube(.7, true, intake));
			runAction(new Clamp(intake, false));
			runAction(new FollowTrajectory("CenterSwitchRight3", drive));
			runAction(new Clamp(intake, true));
			runAction(new IntakeCube(0, true, intake));
			runActionsParallel(new PivotAngle(Constants.PIVOT_POSITION_SWITCH, elevator), new FollowTrajectory("CenterSwitchRight4", drive));
			runAction(new FollowTrajectory("CenterSwitchRight5", drive));
			runAction(new IntakeCube(.7, false, intake));
			runAction(new Wait(1));
			runAction(new IntakeCube(0, true, intake));
		} 
		else {
			runAction(new Clamp(intake, true));
			runAction(new FollowTrajectory("CenterSwitchLeft1", drive));
			runAction(new IntakeCube(.7, false, intake));
			runActionsParallel(new PivotAngle(Constants.PIVOT_POSITION_BOTTOM, elevator), new FollowTrajectory("CenterSwitchLeft2", drive));
			runAction(new IntakeCube(.7, true, intake));
			runAction(new Clamp(intake, false));
			runAction(new FollowTrajectory("CenterSwitchLeft3", drive));
			runAction(new Clamp(intake, true));
			runAction(new IntakeCube(0, true, intake));
			runActionsParallel(new PivotAngle(Constants.PIVOT_POSITION_SWITCH, elevator), new FollowTrajectory("CenterSwitchLeft4", drive));
			runAction(new FollowTrajectory("CenterSwitchLeft5", drive));
			runAction(new IntakeCube(.7, false, intake));
			runAction(new Wait(1));
			runAction(new IntakeCube(0, true, intake));
		}
	}
}
