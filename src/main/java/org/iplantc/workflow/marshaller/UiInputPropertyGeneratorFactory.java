package org.iplantc.workflow.marshaller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.iplantc.files.types.ReferenceGenomeHandler;
import org.iplantc.workflow.dao.DaoFactory;

/**
 * Used to get property generators for specific types of data objects.
 * 
 * @author Dennis Roberts
 */
public class UiInputPropertyGeneratorFactory {

    private static final Class<? extends UiInputPropertyGenerator> DEFAULT_INPUT_PROPERTY_GENERATOR
        = FileUiInputPropertyGenerator.class;

    /**
     * Maps input property types to the classes used to generate them.
     */
    private static final Map<String, Class<? extends UiInputPropertyGenerator>> INPUT_PROPERTY_GENERATOR_MAP
        = new HashMap<String, Class<? extends UiInputPropertyGenerator>>();
    static {
        INPUT_PROPERTY_GENERATOR_MAP.put("ReferenceGenome", ReferenceGenomeUiInputPropertyGenerator.class);
        INPUT_PROPERTY_GENERATOR_MAP.put("ReferenceSequence", ReferenceGenomeUiInputPropertyGenerator.class);
        INPUT_PROPERTY_GENERATOR_MAP.put("ReferenceAnnotation", ReferenceGenomeUiInputPropertyGenerator.class);
    };

    /**
     * Used to get the list of reference genomes for property types that require them.
     */
    private ReferenceGenomeHandler referenceGenomeHandler;

    /**
     * The factory used to generate data access objects.
     */
    private DaoFactory daoFactory;

    /**
     * @param referenceGenomeHandler used to obtain the list of reference genomes when necessary.
     * @param daoFactory the factory used to generate data access objects.
     */
    public UiInputPropertyGeneratorFactory(ReferenceGenomeHandler referenceGenomeHandler, DaoFactory daoFactory) {
        this.referenceGenomeHandler = referenceGenomeHandler;
        this.daoFactory = daoFactory;
    }

    /**
     * Gets the UI input property generator for the given info type name.
     * 
     * @param infoTypeName the info type name.
     * @return the UI input property generator.
     */
    public UiInputPropertyGenerator getUiInputPropertyGenerator(String infoTypeName) {
        Class<? extends UiInputPropertyGenerator> clazz = INPUT_PROPERTY_GENERATOR_MAP.get(infoTypeName);
        if (clazz == null) {
            clazz = DEFAULT_INPUT_PROPERTY_GENERATOR;
        }
        Constructor<? extends UiInputPropertyGenerator> constructor = getConstructor(clazz);
        return createGenerator(constructor);
    }

    /**
     * Creates the new generator.
     * 
     * @param constructor the constructor used to create the generator.
     * @return the generator.
     */
    private UiInputPropertyGenerator createGenerator(Constructor<? extends UiInputPropertyGenerator> constructor) {
        try {
            return constructor.newInstance(referenceGenomeHandler, daoFactory);
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the constructor to call for the given class.
     * 
     * @param clazz the class.
     * @return the constructor.
     */
    private Constructor<? extends UiInputPropertyGenerator> getConstructor(
        Class<? extends UiInputPropertyGenerator> clazz)
    {
        try {
            return clazz.getConstructor(ReferenceGenomeHandler.class, DaoFactory.class);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException("required constructor missing for " + clazz.getName(), e);
        }
    }
}
