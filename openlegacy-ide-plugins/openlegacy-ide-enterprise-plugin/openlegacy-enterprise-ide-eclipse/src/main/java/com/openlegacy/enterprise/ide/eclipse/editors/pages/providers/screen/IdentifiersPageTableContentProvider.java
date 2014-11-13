package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIdentifiersModel;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Ivan Bort
 * 
 */
public class IdentifiersPageTableContentProvider implements IStructuredContentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ScreenIdentifiersModel) {
			ScreenIdentifiersModel model = (ScreenIdentifiersModel)inputElement;
			if ((model != null) && (!model.getIdentifiers().isEmpty())) {
				return model.getIdentifiers().toArray();
			}
		}
		return new Object[] {};
	}

}
