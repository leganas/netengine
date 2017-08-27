package by.legan.library.netengine.interfaces;

public abstract class Net<T> {
	// Храним экземпляр игрового контроллера 
	public T programController;

	public Net(T programController) {
		super();
		this.programController = programController;
	}
}
