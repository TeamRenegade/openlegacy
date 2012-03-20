package org.openlegacy.designtime.terminal.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;

@Component
public class GenerateUtil {

	private File templatesDir;

	public void setTemplateDirectory(File templatesDir) {
		this.templatesDir = templatesDir;
	}

	/**
	 * 
	 * @param model
	 * @param out
	 * @param templateName
	 * @param templatePrefix
	 *            allows to work with template with prefix if exists. e.g: MenuScreenEntityMvcPage.jspx.template /
	 *            ScreenEntityMvcPage.jspx.template for generating a different page type for Menu screens
	 * @throws GenerationException
	 * @throws IOException
	 */
	public void generate(Object model, OutputStream out, String templateName, String templatePrefix) throws GenerationException {

		try {
			Configuration configuration = new Configuration();
			if (isUseCustomTemplates()) {
				configuration.setDirectoryForTemplateLoading(templatesDir);
			} else {
				configuration.setClassForTemplateLoading(GenerateUtil.class, "/");
			}
			configuration.setWhitespaceStripping(true);
			Template template = null;
			try {
				if (isUseCustomTemplates()) {
					template = findTemplate(templateName, templatePrefix, configuration);
				}
			} catch (FileNotFoundException e) {
				// OK
			}
			// pick default template if couldn't find custom template
			if (template == null) {
				URL resource = GenerateUtil.class.getResource(MessageFormat.format("/{0}{1}", templatePrefix, templateName));
				configuration.setClassForTemplateLoading(GenerateUtil.class, "/");
				if (resource != null) {
					template = configuration.getTemplate(templatePrefix + templateName);
				} else {
					template = configuration.getTemplate("/" + templateName);
				}
			}

			OutputStreamWriter output = new OutputStreamWriter(out);
			template.process(model, output);
		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		}
	}

	private boolean isUseCustomTemplates() {
		return templatesDir != null && templatesDir.exists();
	}

	private Template findTemplate(String templateName, String templatePrefix, Configuration configuration) throws IOException {
		Template template = null;
		if (isUseCustomTemplates()) {
			File templateFile = new File(templatesDir, MessageFormat.format("/{0}{1}", templatePrefix, templateName));
			if (templateFile.exists()) {
				template = configuration.getTemplate(templatePrefix + templateName);
			} else {
				template = configuration.getTemplate(templateName);
			}
		} else {
			URL resource = GenerateUtil.class.getResource(MessageFormat.format("/{0}{1}", templatePrefix, templateName));
			if (resource != null) {
				template = configuration.getTemplate(templatePrefix + templateName);
			} else {
				template = configuration.getTemplate(templateName);
			}
		}
		return template;
	}

	private static Template getTemplate(String templateName, String templatePrefix, Configuration configuration,
			boolean templateWithPrefixExists) throws IOException {
		Template template;
		if (templateWithPrefixExists) {
			try {
				template = configuration.getTemplate(templatePrefix + templateName);
			} catch (FileNotFoundException e) {
				// fall back to default template
				template = configuration.getTemplate(templateName);
			}
		} else {
			template = configuration.getTemplate(templateName);
		}
		return template;
	}

	public static void setPackageName(Collection<ScreenEntityDefinition> screenDefinitions, String packageName) {
		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setPackageName(packageName);
		}

	}
}
