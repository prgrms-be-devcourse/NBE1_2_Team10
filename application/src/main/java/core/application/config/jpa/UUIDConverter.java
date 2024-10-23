package core.application.config.jpa;

import java.nio.ByteBuffer;
import java.util.UUID;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UUIDConverter implements AttributeConverter<UUID, byte[]> {

	@Override
	public byte[] convertToDatabaseColumn(UUID uuid) {
		if (uuid == null) {
			return null;
		}
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		return bb.array();
	}

	@Override
	public UUID convertToEntityAttribute(byte[] dbData) {
		if (dbData == null) {
			return null;
		}
		ByteBuffer bb = ByteBuffer.wrap(dbData);
		long mostSigBits = bb.getLong();
		long leastSigBits = bb.getLong();
		return new UUID(mostSigBits, leastSigBits);
	}
}
