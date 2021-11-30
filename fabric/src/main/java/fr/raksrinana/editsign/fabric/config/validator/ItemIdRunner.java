package fr.raksrinana.editsign.fabric.config.validator;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import static java.util.Objects.isNull;

public class ItemIdRunner implements ValidatorRunner<ItemId>{
	private static final Pattern MINECRAFT_ID_PATTERN = Pattern.compile("#?[a-z0-9_.-]+:[a-z0-9/._-]+");
	private static final Component errorText = new TranslatableComponent("text.autoconfig.editsign.error.invalidItemResourceLocation");
	
	@Override
	public Optional<Component> apply(Object value, ItemId annotation){
		if(isNull(value)){
			return Optional.of(errorText);
		}
		if(value instanceof String val){
			if(!annotation.allowEmpty() || !val.isEmpty()){
				if(!MINECRAFT_ID_PATTERN.matcher((String) value).matches()){
					return Optional.of(errorText);
				}
			}
		}
		else if(value instanceof List<?> list){
			var valid = list.stream()
					.filter(Objects::nonNull)
					.map(Object::toString)
					.allMatch(val -> MINECRAFT_ID_PATTERN.matcher(val).matches());
			if(!valid){
				return Optional.of(errorText);
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Class<ItemId> getAnnotationClass(){
		return ItemId.class;
	}
}
