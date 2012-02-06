package org.openlegacy.designtime.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.generators.ScreenEntityMvcGenerator;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedDefinitionUtils;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.ScreenPageBuilder;
import org.openlegacy.terminal.layout.mock.ScreenForPage;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntityMvcGeneratorTest {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;
	@Inject
	private ScreenPageBuilder screenPageBuilder;

	@Test
	public void testGenerateJspx() throws Exception {

		ScreenEntityDefinition screen1Definition = screenEntitiesRegistry.get(ScreenForPage.class);

		assertPageGeneration(screen1Definition);
	}

	private void assertPageGeneration(ScreenEntityDefinition screen1Definition) throws TemplateException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screen1Definition);
		new ScreenEntityMvcGenerator().generateJspx(pageDefinition, baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPage.jspx.expected"));

		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@Test
	public void testGenerateJspxByCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/terminal/layout/mock/ScreenForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		ScreenEntityDefinition screenDefinition = CodeBasedDefinitionUtils.getEntityDefinition(compilationUnit);
		assertPageGeneration(screenDefinition);
	}
}
