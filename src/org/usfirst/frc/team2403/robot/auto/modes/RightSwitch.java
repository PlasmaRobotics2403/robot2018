
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.auto.actions.DriveStraight;
import org.usfirst.frc.team2403.robot.auto.actions.IntakeCube;
import org.usfirst.frc.team2403.robot.auto.actions.TurnAngle;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;

public class RightSwitch extends AutoMode{ 

	DriveTrain drive;
	Intake intake;
	
	String gameData;
	
	public RightSwitch(DriveTrain drive, Intake intake) {
		this.drive = drive;
		this.intake = intake;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}

	@Override
	protected void routine() throws AutoModeEndedException {		
		if(gameData.charAt(0) == 'L') {
			runAction(new DriveStraight(.6, 150, drive));
			runAction(new TurnAngle(.9, 90, drive));
			runAction(new DriveStraight(.6, 150, drive));
			runAction (new TurnAngle(.9, 90, drive));
			runAction(new DriveStraight(.6, 30, drive));
			runAction(new IntakeCube(.75, true, intake));
		} 
		else {
			runAction(new DriveStraight(.6, 100, drive));
			runAction(new TurnAngle(.9, 90, drive));
			runAction(new DriveStraight(.6, 7, drive)); 
			runAction(new IntakeCube(.75, true, intake));
		}
	} 
}
