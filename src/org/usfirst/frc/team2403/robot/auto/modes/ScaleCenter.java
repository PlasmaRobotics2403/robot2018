
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.Elevator;
import org.usfirst.frc.team2403.robot.auto.actions.DriveStraight;
import org.usfirst.frc.team2403.robot.auto.actions.TurnAngle;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;

public class ScaleCenter extends AutoMode{ 

	DriveTrain drive;
	Elevator elevator;
	
	String gameData;
	
	public ScaleCenter(DriveTrain drive, Elevator elevator) {
		this.drive = drive;
		this.elevator = elevator;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		if(gameData.charAt(0) == 'L') {
			runAction(new DriveStraight(.4, 110, drive));
			runAction(new TurnAngle(.4, -90, drive));
			runAction(new DriveStraight(.4, 110, drive));
			runAction (new TurnAngle(.4, 90, drive));
		} 
		else {
			runAction(new DriveStraight(.4, 110, drive));
			runAction(new TurnAngle(.4, 90, drive));
			runAction(new DriveStraight(.4, 110, drive));
			runAction (new TurnAngle(.4, -90, drive));
		}
		
	}
	
}
