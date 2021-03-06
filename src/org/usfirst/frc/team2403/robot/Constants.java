package org.usfirst.frc.team2403.robot;

public class Constants {
	
	/* CONTROLLER CONSTANTS */
	public static final int JOYSTICK1_PORT = 0;
	
	
	/* TALON ID CONSTANTS */
	public static final int L_DRIVE_ID = 1;
	public static final int L_DRIVE_SLAVE_ID = 2;
	public static final int R_DRIVE_ID = 3;
	public static final int R_DRIVE_SLAVE_ID = 4;
	public static final int INTAKE_LEFT_ID = 5;
	public static final int INTAKE_RIGHT_ID = 6;
	public static final int ELEV_PIVOT_LEFT_ID = 7;
	public static final int ELEV_PIVOT_RIGHT_ID = 8;
	public static final int ELEV_LIFT_LEFT = 9;
	public static final int ELEV_LIFT_RIGHT = 10;	
	
	/*DRIVETRAIN CONSTANTS*/
	public static final double MAX_AUTO_DRIVE_SPEED = .9;
	public static final double MAX_DRIVE_SPEED = 1;
	public static final double MAX_DRIVE_TURN = 1;	
	public static final double DRIVE_ENCODER_CONVERSION = 0.0045002849;
	
	
	/*ELEVATOR CONSTANTS*/
	public static final double MAX_PIVOT_SPEED = -.7;
	public static final double MAX_LIFT_SPEED = .7;
	public static final int PIVOT_LIMIT_ID = 0;
	public static final int LIFT_LIMIT_ID = 1;
	public static final double PIVOT_RAMP_RATE = .01;
	public static final double LIFT_RAMP_RATE = .02;
	public static final double LIFT_ENCODER_CONVERSION = 0.001433178;
	public static final double PIVOT_ENCODER_CONVERSION = -0.0462249614;
	public static final int PIVOT_POSITION_BOTTOM = 0;
	public static final int PIVOT_POSITION_SECOND = -290;
	public static final int PIVOT_POSITION_SWITCH = -900;
	public static final int PIVOT_POSITION_AUTO = -1240;
	public static final int PIVOT_POSITION_SCALE = -1500;
	public static final int PIVOT_POSITION_CLIMB = -1780;
	
	/*INTAKE CONSTANTS*/
	public static final double MAX_INTAKE_SPEED = -.85;
	
	/*PNEUMATIC CONSTANTS*/
	public static final int CLAMP_OUT = 0;
	public static final int CLAMP_IN = 1;
	public static final int PIVOT_OUT = 2;
	public static final int PIVOT_IN = 3;
	public static final int LIFT_OUT = 4;
	public static final int LIFT_IN = 5;
	public static final int HELP_EXTEND = 6;
	public static final int HELP_RETRACT = 7;
	
	/*TALON CONFIG CONSTANTS*/
	public static final int TALON_TIMEOUT = 10;
}
