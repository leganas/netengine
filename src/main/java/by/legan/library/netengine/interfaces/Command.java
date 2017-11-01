package by.legan.library.netengine.interfaces;

/**Абстрактный класс описывающий Event типа Команда для чего либо*/
public abstract class Command<T> extends Event{
	public abstract Event ApplyCommand(T controller);
	
	@Override
	public Event Apply(ProgramController<?> programController, int id) {
		return  ApplyCommand((T) programController);
	}

}
