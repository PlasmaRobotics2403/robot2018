/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.controllers.PlasmaJoystick;
import org.usfirst.frc.team2403.robot.auto.modes.*;
import org.usfirst.frc.team2403.robot.auto.util.*;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends IterativeRobot {
	
	PlasmaJoystick joystick;
	DriveTrain driveTrain;
	Elevator elevator;
	Intake intake;
	Compressor compressor;
	
	AutoModeRunner autoModeRunner;
	
	AutoMode[] autoModes;
	int autoModeSelection;
	
	//private static final String kDefaultAuto = "Default";
	//private static final String kCustomAuto = "My Auto";
	//private String m_autoSelected;
	//private SendableChooser<String> m_chooser = new SendableChooser<>();

	
	@Override
	public void robotInit() {
		
		joystick = new PlasmaJoystick(Constants.JOYSTICK1_PORT);
		
		driveTrain = new DriveTrain(Constants.L_DRIVE_ID,
									Constants.L_DRIVE_SLAVE_ID, 
									Constants.R_DRIVE_ID, 
									Constants.R_DRIVE_SLAVE_ID);
		
		intake = new Intake(Constants.INTAKE_LEFT_ID,
										  Constants.INTAKE_RIGHT_ID);
		
		elevator = new Elevator(Constants.ELEV_PIVOT_LEFT_ID,
								Constants.ELEV_PIVOT_RIGHT_ID,
								Constants.ELEV_LIFT_LEFT,
								Constants.ELEV_LIFT_RIGHT);
		
		compressor = new Compressor(0);
		
		compressor.setClosedLoopControl(true);
		
		autoModeRunner = new AutoModeRunner();
		autoModes = new AutoMode[2];
		for(int i = 0; i < autoModes.length; i++){
			autoModes[i] = new Nothing();
		}

		autoModeSelection = 0;
		SmartDashboard.putNumber("Auto Mode", 0);
		
		//m_chooser.addDefault("Default Auto", kDefaultAuto);
		//m_chooser.addObject("My Auto", kCustomAuto);
		//SmartDashboard.putData("Auto choices", m_chooser);
	}
	
	public void robotPeriodic() {
		elevator.reportPivotData();
	}
	
	public void disabledInit() {
		autoModeRunner.stop();
		intake.clamp();
	}
	
	public void disabledPeriodic() {
		autoModeSelection = (int)SmartDashboard.getNumber("Auto Mode", 0);
	}
		
	@Override
	public void autonomousInit() {
		
		autoModes[0] = new CrossBaseline(driveTrain);
		autoModes[1] = new SwitchRight(driveTrain, intake);
		autoModeSelection = (autoModeSelection >= autoModes.length) ? 0 : autoModeSelection;
		autoModeSelection = (autoModeSelection < 0) ? 0 : autoModeSelection;
		autoModeRunner.chooseAutoMode(autoModes[1]);  //autoModeSelection]);
		autoModeRunner.start();
		
		//m_autoSelected = m_chooser.getSelected();
		 //m_autoSelected = SmartDashboard.getString("Auto Selector",
		 //kDefaultAuto);
		//System.out.println("Auto selected: " + m_autoSelected);
	}

	
	@Override
	public void autonomousPeriodic() {


		
		/*switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				
				break;
		} */
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		driver1Controls(joystick);
	}

	
	public void driver1Controls(PlasmaJoystick joy) {

		driveTrain.FPSDrive(joystick.LeftY, joystick.RightX);
		elevator.PivotRotate(joystick.LT, joystick.RT);
		if(joystick.LB.isPressed()) {
			intake.in(1);
		}
		else if(joystick.RB.isPressed()) {
			intake.out(1);
		}
		else if(joystick.dPad.getPOV() > 45 && joystick.dPad.getPOV() < 135) {
			intake.spinRight(1);
		}
		else if(joystick.dPad.getPOV() > 225 && joystick.dPad.getPOV() < 315) {
			intake.spinLeft(1);
		}
		else {
			intake.out(0);
			intake.in(0);
		}
		
		if(joystick.Y.isPressed()) {
			elevator.releaseLiftBrake();
			elevator.extend(1);
		}
		else if(joystick.A.isPressed()) {
			elevator.releaseLiftBrake();
			elevator.retract(1);
		}
		else {
			elevator.activateLiftBrake();
			elevator.extend(0);
			elevator.retract(0);
		}
		
		if(joystick.X.isPressed()) {
			intake.clamp();
		}
		else if(joystick.B.isPressed()) {
			intake.release();
		}
		else {
			
		}
	}
	
	@Override
	public void testPeriodic() {
	}
}