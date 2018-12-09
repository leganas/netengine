package by.legan.library.netengine.interfaces;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**Абстрактный контроллер*/
public abstract class ProgramController<T> implements Disposable {

	public enum Status {
		Stop,
		Init,
		Start,
		Run,
		Dispose
	}

	// Имя потока обновления текущего контроллера
	String name;
	// Статус состояния контроллера в котором он находится
	protected Status status = Status.Stop;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	long sleepMillis=0;
	int sleepNano=500;

	public long getSleepMillis() {
		return sleepMillis;
	}

	public void setSleepMillis(long sleepMillis) {
		this.sleepMillis = sleepMillis;
	}

	public int getSleepNano() {
		return sleepNano;
	}

	public void setSleepNano(int sleepNano) {
		this.sleepNano = sleepNano;
	}

	boolean updateRun = false; // Флаг определяющий работу фонового потока обновления

	private class Update implements Runnable {
		@Override
		public void run() {
			while (updateRun) {
				update();
				try {
					Thread.sleep(sleepMillis, sleepNano);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Logs.out("Thread " + threadUpdate.getName() + " stop");
			if (status == Status.Dispose) {
			//	System.exit(0);
			}
		}
	}

	public void startUpdateThread(String name){
		this.name = name;
		threadUpdate = new Thread(new Update());
		threadUpdate.setName("Update | " + this.name);
		Logs.out("Thread " + threadUpdate.getName() + " run");
		updateRun = true;
		threadUpdate.start();
		status = Status.Run;
	}

	public void stopUpdateThread(){
		status = Status.Stop;
		updateRun = false;
	}

	private T items;
	Thread threadUpdate;

	public ProgramController(){
	}

	public ProgramController(String name){
		this.name = name;
		status = Status.Init;
	}

	public T getItems() {
		return (T) items;
	}

	public void setItems(T items) {
		this.items = items;
	}

	/**
	 * Вызывается в фоновом потоке с интервалом 	long sleepMillis=0;	 int sleepNano=500;
	 * */
	public abstract void update();

	public void dispose() {
		stopUpdateThread();
		status = Status.Dispose;
	}
	
}
