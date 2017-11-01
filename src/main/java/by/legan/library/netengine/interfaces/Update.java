package by.legan.library.netengine.interfaces;

import java.util.ArrayList;

/**
 * Абстрактый класс содержащий обновления*/
public abstract class Update<T> extends Event {
	public ArrayList<T> update;
}
