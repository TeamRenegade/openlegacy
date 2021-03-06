package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class GeneralMasterBlockContentProvider implements IStructuredContentProvider {

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
		if (!(inputElement instanceof RpcEntity)) {
			return new Object[] {};
		}
		RpcEntity entity = (RpcEntity)inputElement;
		List<Object> list = new ArrayList<Object>();
		list.add(entity.getEntityModel().clone());
		list.add(entity.getNavigationModel().clone());
		return list.toArray();
	}

}
