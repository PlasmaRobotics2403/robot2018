package org.usfirst.frc.team2403.robot.auto.util;

import edu.wpi.first.wpilibj.DriverStation;

public abstract class AutoMode {
	protected double updateRate = 1.0/50.0;
	protected boolean isActive = false;
	
	/**
	 * This should be defined in each auto mode class. It should be a series of runAction(action) calls.
	 * @throws AutoModeEndedException
	 * @author Nic A
	 */
	protected abstract void routine() throws AutoModeEndedException;
	
	/**
	 * This is responsible for running the routine. It will kill the auto mode early if an AutoModeEndedException is thrown.
	 * @author Nic A
	 */
	public void run(){
		isActive = true;
		try{
			routine();
		}
		catch (AutoModeEndedException e){
			DriverStation.reportWarning("Auto Mode ended early", true);
			return;
		}
	}
	
	/**
	 * Call this to check if the auto code is active.
	 * @return True if code is active. False otherwise.
	 * @author Nic A
	 */
	public boolean isActive(){
		return isActive;
	}
	
	/**
	 * Checks if auto mode is active. If supposed to be inactive, it throws AutoModeEndedException to ensure code is dead.
	 * @return True if code is active. False otherwise.
	 * @throws AutoModeEndedException
	 * @author Nic A
	 */
	protected boolean isActiveWithThrow() throws AutoModeEndedException{
		if(!isActive()){
			throw new AutoModeEndedException();
		}
		return isActive();
	}
	
	/**
	 * This runs a single action. This should only be called from within the routine() method
	 * @param action The action to run
	 * @throws AutoModeEndedException
	 *
	 * @author Nic A
	 */
	protected void runAction(Action action) throws AutoModeEndedException{
		isActiveWithThrow();
		action.start();
		long waitTime = (long)(updateRate * 1000.0);
		while(isActive() && !action.isFinished()){
			action.update();
			try{
				Thread.sleep(waitTime);
			}
			catch(InterruptedException e){
				stop();
			}
		}
		action.end();
		isActiveWithThrow();
	}
	
	/**
	 * This stops the auto mode.
	 *
	 * @author Nic A
	 */
	public void stop(){
		isActive = false;
	}
	
}
