package org.usfirst.frc.team2403.robot;

public class Constants {
	
	/* CONTROLLER CONSTANTS */
	public static final int JOYSTICK1_PORT = 0;
	
	
	/* TALON ID CONSTANTS */
	public static final int L_DRIVE_ID = 2;
	public static final int L_DRIVE_SLAVE_ID = 1;
	public static final int R_DRIVE_ID = 4;
	public static final int R_DRIVE_SLAVE_ID = 3;
	public static final int ELEV_PIVOT_LEFT_ID = 5;
	public static final int ELEV_PIVOT_RIGHT_ID = 6;
	public static final int INTAKE_LEFT_ID = 7;
	public static final int INTAKE_RIGHT_ID = 8;
	public static final int ELEV_LIFT_LEFT = 9;
	public static final int ELEV_LIFT_RIGHT = 10;	
	
	/*DRIVETRAIN CONSTANTS*/
	public static final double MAX_AUTO_DRIVE_SPEED = .9;
	public static final double MAX_DRIVE_SPEED = 1;
	public static final double MAX_DRIVE_TURN = 1;	
	public static final double DRIVE_ENCODER_CONVERSION = 0.0048334959;
	
	/*ELEVATOR CONSTANTS*/
	public static final double MAX_PIVOT_SPEED = .7;
	public static final double MAX_LIFT_SPEED = .7;
	
	/*INTAKE CONSTANTS*/
	public static final double MAX_INTAKE_SPEED = .8;
	
}
