package edu.arizona.biosemantics.micropie.web.server.process.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import edu.arizona.biosemantics.micropie.web.server.Configuration;
import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.micropie.web.shared.model.file.FileTypeEnum;

public class XmlNamespaceManager {

	private SAXBuilder sax = new SAXBuilder();
	private Namespace bioNamespace = Namespace.getNamespace("bio", Configuration.targetNamespace);
	private Namespace xsiNamespace = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

	private Map<FileTypeEnum, String> fileTypeSchemaMap = new HashMap<FileTypeEnum, String>(); 
	private Map<String, FileTypeEnum> schemaFileTypeMap = new HashMap<String, FileTypeEnum>(); 
	
	public XmlNamespaceManager() {
		fileTypeSchemaMap.put(FileTypeEnum.TAXON_DESCRIPTION, Configuration.taxonDescriptionSchemaFileWeb);
		fileTypeSchemaMap.put(FileTypeEnum.MARKED_UP_TAXON_DESCRIPTION, Configuration.markedUpTaxonDescriptionSchemaFileWeb);
		schemaFileTypeMap.put(Configuration.taxonDescriptionSchemaFileWeb, FileTypeEnum.TAXON_DESCRIPTION);
		schemaFileTypeMap.put(Configuration.markedUpTaxonDescriptionSchemaFileWeb, FileTypeEnum.MARKED_UP_TAXON_DESCRIPTION);
	}
		
	public FileTypeEnum getFileType(File file) { 
		String schema = this.getSchema(file);
		if(schema != null)
			return schemaFileTypeMap.get(schema);
		return null;
	}
	
	public String getSchema(File file) {
		Document doc = null;
		try {
			doc = sax.build(file);
		} catch (JDOMException | IOException e) {
			//log.ERROR, "Couldn't build xml document", e);
		}
		if(doc != null)
			return getSchema(doc);
		return null;
	}
	
	private String getSchema(Document doc) {
		Element rootElement = doc.getRootElement();
		return rootElement.getAttributeValue("schemaLocation", xsiNamespace).replace(Configuration.targetNamespace, "").trim();
	}

	public String getSchema(FileTypeEnum fileTypeEnum) {
		return fileTypeSchemaMap.get(fileTypeEnum);
	}
	
	public String getSchema(String fileContent) {
		try (StringReader reader = new StringReader(fileContent)) {
			Document doc = sax.build(reader);
			return getSchema(doc);
		} catch (JDOMException | IOException e) {
			//log(LogLevel.ERROR, "Couldn't build xml document", e);
		}
		return null;
	}
	
	public void setXmlSchema(File file, FileTypeEnum fileTypeEnum) {
		Document doc = null;
		try {
			doc = sax.build(file);
		} catch (JDOMException | IOException e) {
			//log(LogLevel.ERROR, "Couldn't build xml document", e);
		}
		if(doc != null) { 
			setXmlSchema(doc, fileTypeEnum);
			try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
				try {
					xmlOutputter.output(doc, fileOutputStream);
				} catch (IOException e) {
					//log(LogLevel.ERROR, "Couldn't output xml document to file", e);
				}
			} catch (IOException e) {
				//log(LogLevel.ERROR, "Couldn't create or close outputstream", e);
			}
		}
	}

	public void setXmlSchema(Document doc, FileTypeEnum fileTypeEnum) {
		String schemaUrl = getSchema(fileTypeEnum);
		Element rootElement = doc.getRootElement();
		rootElement.setNamespace(bioNamespace);
		rootElement.addNamespaceDeclaration(bioNamespace);
		rootElement.addNamespaceDeclaration(xsiNamespace);
		rootElement.setAttribute("schemaLocation", Configuration.targetNamespace + " " + schemaUrl, xsiNamespace);
	}

	public String setXmlSchema(String content, FileTypeEnum fileTypeEnum) {
		try(StringReader reader = new StringReader(content)) {
			Document doc = null;
			try {
				doc = sax.build(reader);
			} catch (JDOMException | IOException e) {
				//log(LogLevel.ERROR, "Couldn't build xml document", e);
			}
			if(doc != null) {
				setXmlSchema(doc, fileTypeEnum);
				
				try(StringWriter stringWriter = new StringWriter()) {
					try {
						xmlOutputter.output(doc, stringWriter);
					} catch (IOException e) {
						//log(LogLevel.ERROR, "Couldn't output xml document", e);
					}
					stringWriter.flush();
					String result = stringWriter.toString();
					return result;
				} catch (IOException e) {
					//log(LogLevel.ERROR, "Couldn't open or close writer", e);
				}
			}
		}
		return null;
	}
	
	public void removeXmlSchema(File file) {
		Document doc = null;
		try {
			doc = sax.build(file);
		} catch (JDOMException | IOException e) {
			//log(LogLevel.ERROR, "Couldn't build xml document", e);
		}
		if(doc != null) {
			Element rootElement = doc.getRootElement();
			rootElement.setNamespace(null);
			rootElement.removeNamespaceDeclaration(bioNamespace);
			rootElement.removeNamespaceDeclaration(xsiNamespace);
			rootElement.removeAttribute("schemaLocation", xsiNamespace);
			try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
				try {
					xmlOutputter.output(doc, fileOutputStream);
				} catch (IOException e) {
					//log(LogLevel.ERROR, "Couldn't output xml document", e);
				}
			} catch (IOException e) {
				//log(LogLevel.ERROR, "Couldn't open or close output stream", e);
			}
		}
	}
	
}
