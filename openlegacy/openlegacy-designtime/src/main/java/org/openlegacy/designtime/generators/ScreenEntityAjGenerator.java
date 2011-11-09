package org.openlegacy.designtime.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class ScreenEntityAjGenerator {

	public void generate(File javaFile) throws IOException, TemplateException, ParseException {

		FileInputStream input = new FileInputStream(javaFile);

		CompilationUnit compilationUnit = JavaParser.parse(input);

		List<TypeDeclaration> types = compilationUnit.getTypes();
		for (TypeDeclaration typeDeclaration : types) {
			List<AnnotationExpr> annotations = typeDeclaration.getAnnotations();
			if (annotations == null) {
				continue;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (AnnotationExpr annotationExpr : annotations) {
				ScreenEntityCodeModel screenEntityCodeModel = null;
				if (hasAnnotation(annotationExpr, AnnotationConstants.SCREEN_ENTITY_ANNOTATION)) {
					screenEntityCodeModel = generateScreenEntity(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
							baos);
					checkInnerClasses(javaFile, compilationUnit, typeDeclaration);
				}
				if (hasAnnotation(annotationExpr, AnnotationConstants.SCREEN_PART_ANNOTATION)) {
					screenEntityCodeModel = generateScreenPart(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
							baos, "");
				}
				if (hasAnnotation(annotationExpr, AnnotationConstants.SCREEN_TABLE_ANNOTATION)) {
					screenEntityCodeModel = generateScreenTable(compilationUnit, (ClassOrInterfaceDeclaration)typeDeclaration,
							baos, "");
				}
				writeToFile(javaFile, baos, screenEntityCodeModel);
			}
		}

	}

	private static boolean hasAnnotation(AnnotationExpr annotationExpr, String annotation) {
		return annotationExpr.getName().getName().equals(annotation);
	}

	private void checkInnerClasses(File javaFile, CompilationUnit compilationUnit, TypeDeclaration parentType)
			throws IOException, TemplateException, ParseException, FileNotFoundException {
		List<BodyDeclaration> members = parentType.getMembers();

		for (BodyDeclaration bodyDeclaration : members) {
			if (bodyDeclaration instanceof ClassOrInterfaceDeclaration) {

				List<AnnotationExpr> annotations = ((ClassOrInterfaceDeclaration)bodyDeclaration).getAnnotations();
				for (AnnotationExpr annotationExpr : annotations) {
					ScreenEntityCodeModel screenEntityCodeModel = null;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					if (hasAnnotation(annotationExpr, AnnotationConstants.SCREEN_PART_ANNOTATION)) {
						screenEntityCodeModel = generateScreenPart(compilationUnit, (ClassOrInterfaceDeclaration)bodyDeclaration,
								baos, parentType.getName());
					}
					if (hasAnnotation(annotationExpr, AnnotationConstants.SCREEN_TABLE_ANNOTATION)) {
						screenEntityCodeModel = generateScreenTable(compilationUnit,
								(ClassOrInterfaceDeclaration)bodyDeclaration, baos, parentType.getName());
					}
					writeToFile(javaFile, baos, screenEntityCodeModel);
				}
			}
		}
	}

	public ScreenEntityCodeModel generateScreenEntity(CompilationUnit compilationUnit,
			ClassOrInterfaceDeclaration typeDeclaration, OutputStream out) throws IOException, TemplateException, ParseException {
		return generate(out, compilationUnit, "", typeDeclaration, "Screen_Aspect.aj.template");
	}

	public ScreenEntityCodeModel generateScreenPart(CompilationUnit compilationUnit, ClassOrInterfaceDeclaration typeDeclaration,
			OutputStream out, String parentClass) throws IOException, TemplateException, ParseException {
		return generate(out, compilationUnit, parentClass + ".", typeDeclaration, "ScreenPart_Aspect.aj.template");
	}

	public ScreenEntityCodeModel generateScreenTable(CompilationUnit compilationUnit,
			ClassOrInterfaceDeclaration typeDeclaration, OutputStream out, String parentClass) throws IOException,
			TemplateException, ParseException {
		return generate(out, compilationUnit, parentClass + ".", typeDeclaration, "ScreenTable_Aspect.aj.template");
	}

	private static void writeToFile(File javaFile, ByteArrayOutputStream baos, ScreenEntityCodeModel screenEntityCodeModel)
			throws FileNotFoundException, IOException {
		if (screenEntityCodeModel != null && screenEntityCodeModel.isRelevant()) {
			File outputFolder = javaFile.getParentFile().getAbsoluteFile();
			File outputFile = new File(outputFolder, screenEntityCodeModel.getClassName() + "_Aspect.aj");
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(baos.toByteArray());
			fos.close();
		}
	}

	public ScreenEntityCodeModel generate(OutputStream out, CompilationUnit compilationUnit, String classPrefix,
			ClassOrInterfaceDeclaration typeDeclaration, String templateFileName) throws IOException, TemplateException,
			ParseException {

		String className = classPrefix + typeDeclaration.getName();
		ScreenEntityCodeModel screenEntityCodeModel = new ScreenEntityCodeModelImpl(compilationUnit, typeDeclaration, className);

		if (!screenEntityCodeModel.isRelevant()) {
			return screenEntityCodeModel;
		}

		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(getClass(), "/");

		Template template = configuration.getTemplate(templateFileName);

		OutputStreamWriter output = new OutputStreamWriter(out);

		template.process(screenEntityCodeModel, output);

		return screenEntityCodeModel;
	}

}
