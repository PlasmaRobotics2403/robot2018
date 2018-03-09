
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.auto.actions.DriveStraight;
import org.usfirst.frc.team2403.robot.auto.actions.ReleaseCube;
import org.usfirst.frc.team2403.robot.auto.actions.TurnAngle;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;

public class SwitchRight extends AutoMode{ 

	DriveTrain drive;
	Intake intake;
	
	String gameData;
	
	public SwitchRight(DriveTrain drive, Intake intake) {
		this.drive = drive;
		this.intake = intake;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}

	@Override
	protected void routine() throws AutoModeEndedException {		
		if(gameData.charAt(0) == 'L') {
			runAction(new DriveStraight(.4, 176, drive));
			runAction(new TurnAngle(.2, 90, drive));
			runAction(new DriveStraight(.3, 150, drive));
			runAction (new TurnAngle(.3, 45, drive));
			runAction(new ReleaseCube(-1, intake));
		} 
		else {		
			runAction(new DriveStraight(.4, 105, drive));
			runAction(new TurnAngle(.3, 75, drive));
			runAction(new ReleaseCube(-1, intake));
		}
	}
	
}
