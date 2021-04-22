package cplex.java.customdatasource.model;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;

public class SpendoptiDeserializerContext extends DefaultDeserializationContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpendoptiDeserializerContext(SpendoptiModelRequest modelInput) {
		super(BeanDeserializerFactory.instance, null);
	}

	public SpendoptiDeserializerContext(DeserializerFactory df) {
		super(df, null);
	}

	protected SpendoptiDeserializerContext(SpendoptiDeserializerContext src, DeserializationConfig config,
			JsonParser jp, InjectableValues values) {
		super(src, config, jp, values);

	}

	protected SpendoptiDeserializerContext(SpendoptiDeserializerContext src, DeserializerFactory factory) {
		super(src, factory);

	}

	protected SpendoptiDeserializerContext(DefaultDeserializationContext src) {
		super(src);
	}

	@Override
	public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp,
			InjectableValues values) {
		return new SpendoptiDeserializerContext(this, config, jp, values);
	}

	@Override
	public DefaultDeserializationContext with(DeserializerFactory factory) {
		return new SpendoptiDeserializerContext(this, factory);
	}

	// Code to use for Jackson > 2.4.0
	@Override
	public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> generator, ObjectIdResolver resolverType) {
		ReadableObjectId oid = super.findObjectId(id, generator, resolverType);
		if (oid == null || oid.resolve() == null) {
			Object object = findObject(generator.getScope(), id);
			if (object != null)
				try {
					oid.bindItem(object);
				} catch (IOException ex) {
					throw new IllegalStateException("Unable to bind " + object + " to " + id, ex);
				}
		}
		return oid;
	}

	protected Object findObject(Class<?> scope, Object id) {
		
		return null;
	}

}
