package com.openlegacy.enterprise.ide.eclipse.ws.generator.actions;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.EntitiesFetcher;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.dialogs.GenerateServiceDialog;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.dialogs.jpa.GenerateJpaServiceDialog;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.LoadingModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog.TreeViewerModel;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.openlegacy.designtime.enums.BackendSolution;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
import org.openlegacy.ide.eclipse.util.PathsUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class GenerateServiceAction extends AbstractAction {

	private GenerateServiceDialog dialog = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	@Override
	public void run(IAction action) {
		final IProject project = getProject();
		File projectPath = PathsUtil.toOsLocation(project);

		final BackendSolution serviceType = EclipseDesignTimeExecuter.instance().getServiceType(projectPath);

		ArrayList<AbstractEntityModel> list = new ArrayList<AbstractEntityModel>();
		list.add(new LoadingModel());
		TreeViewerModel treeViewerModel = new TreeViewerModel(list);
		if (serviceType.equals(BackendSolution.JDBC)) {
			dialog = new GenerateJpaServiceDialog(getShell(), treeViewerModel, project, serviceType);
		} else {
			dialog = new GenerateServiceDialog(getShell(), treeViewerModel, project, serviceType);
		}

		Job job = new Job(Messages.getString("job.processing.entities")) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					int length = project.members().length;
					monitor.beginTask(Messages.getString("task.processing"), length);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				EntitiesFetcher fetcher = new EntitiesFetcher();
				final List<AbstractEntityModel> entities = fetcher.fetch(project, monitor, serviceType);
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						dialog.setTreeViewerInput(new TreeViewerModel(entities));
					}
				});
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.schedule();

		dialog.open();
	}

	private IProject getProject() {
		IProject project = null;
		IStructuredSelection structSelecetion = (IStructuredSelection)getSelection();
		Object firstElement = structSelecetion.getFirstElement();
		if (firstElement instanceof IAdaptable) {
			project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
		}
		return project;
	}

}
