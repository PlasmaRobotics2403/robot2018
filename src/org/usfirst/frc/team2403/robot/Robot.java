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
								Constants.ELEV_LIFT_RIGHT,
								Constants.PIVOT_LIMIT_ID,
								Constants.LIFT_LIMIT_ID);
		
		compressor = new Compressor(0);
		
		compressor.setClosedLoopControl(true);
		
		autoModeRunner = new AutoModeRunner();
		autoModes = new AutoMode[4];
		for(int i = 0; i < autoModes.length; i++){
			autoModes[i] = new Nothing();
		}

		autoModeSelection = 0;
		SmartDashboard.putNumber("Auto Mode", 0);
		
		//m_chooser.addDefault("Default Auto", kDefaultAuto);
		//m_chooser.addObject("My Auto", kCustomAuto);
		//SmartDashboard.putData("Auto choices", m_chooser);
		
		driveTrain.resetEncoders();
	}
	
	public void robotPeriodic() {
		elevator.reportPivotData();
		SmartDashboard.putNumber("GyroAngle", driveTrain.getGyroAngle());
	}
	
	public void disabledInit() {
		compressor.start();
		autoModeRunner.stop();
		//intake.clamp();
		driveTrain.zeroGyro();
	}
	
	public void disabledPeriodic() {
		autoModeSelection = (int)SmartDashboard.getNumber("Auto Mode", 0);
	}
		
	@Override
	public void autonomousInit() {
		driveTrain.resetEncoders();
		compressor.start();
		driveTrain.zeroGyro();
		
		autoModes[0] = new CrossBaseline(driveTrain);
		autoModes[1] = new SwitchRight(driveTrain, intake);
		autoModes[2] = new SwitchLeft(driveTrain, intake);
		autoModes[3] = new SwitchCenter(driveTrain, intake);
		
		autoModeSelection = (autoModeSelection >= autoModes.length) ? 0 : autoModeSelection;
		autoModeSelection = (autoModeSelection < 0) ? 0 : autoModeSelection;
		autoModeRunner.chooseAutoMode(autoModes[autoModeSelection]);
		
		autoModeRunner.chooseAutoMode(new TrajectoryTest(driveTrain, elevator, intake));

		autoModeRunner.start();
		
		//m_autoSelected = m_chooser.getSelected();
		 //m_autoSelected = SmartDashboard.getString("Auto Selector",
		 //kDefaultAuto);
		//System.out.println("Auto selected: " + m_autoSelected);
	}

	
	@Override
	public void autonomousPeriodic() {
		driveTrain.getDistance();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		driver1Controls();
		driveTrain.getDistance();
	}

	
	public void driver1Controls() {
		
		driveTrain.FPSDrive(joystick.LeftY, joystick.RightX);
		if(joystick.BACK.isToggled()) {
			elevator.pivotRotate(joystick.RT, joystick.LT);
			elevator.setPivotTarget(0);
		}
		else {
			elevator.pivotUpdate();
			if(joystick.dPad.getPOV() == 180) {
				elevator.setPivotTarget(Constants.PIVOT_POSITION_BOTTOM);
			}
			else if(joystick.dPad.getPOV() == 270) {
				elevator.setPivotTarget(Constants.PIVOT_POSITION_SECOND);
			}
			else if(joystick.dPad.getPOV() == 90) {
				elevator.setPivotTarget(Constants.PIVOT_POSITION_SWITCH);
			}
			else if(joystick.dPad.getPOV() == 0) {
				elevator.setPivotTarget(Constants.PIVOT_POSITION_SCALE);
			}
			else if(joystick.START.isPressed()) {
				elevator.setPivotTarget(Constants.PIVOT_POSITION_CLIMB);
			}
		}
		
		
		
		if(joystick.LB.isPressed()) {
			intake.out(1);
		}
		else if(joystick.RB.isPressed()) {
			intake.in(1);
		}
		else if(joystick.B.isPressed()) {
			intake.spinLeft(1);
		}
		else if(joystick.X.isPressed()) {
			intake.spinRight(1);
		}
		else {
			intake.out(0);
			intake.in(0);
		}
		
		if(joystick.Y.isPressed()) {
			elevator.extend(1);
		}
		else if(joystick.A.isPressed()) {
			elevator.retract(1);
		}
		else {
			elevator.activateLiftBrake();
			elevator.extend(0);
			elevator.retract(0);
		}
		
		
		if(joystick.R3.isPressed()) {
			intake.clamp();
		}
		else if(joystick.L3.isPressed()) {
			intake.release();
		}
	}
	
	@Override
	public void testInit() {
		driveTrain.FPSDrive(0, 0);
		autoModeRunner.chooseAutoMode(new GenerateTrajectories());

		autoModeRunner.start();
	}
	
	@Override
	public void testPeriodic() {
		driveTrain.FPSDrive(0, 0);
	}
}