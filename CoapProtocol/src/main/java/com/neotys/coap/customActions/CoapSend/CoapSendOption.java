package com.neotys.coap.customActions.CoapSend;

import com.neotys.action.argument.ArgumentValidator;
import com.neotys.action.argument.Option;
import com.neotys.extensions.action.ActionParameter;

import static com.neotys.action.argument.DefaultArgumentValidator.*;
import static com.neotys.action.argument.Option.AppearsByDefault.False;
import static com.neotys.action.argument.Option.AppearsByDefault.True;
import static com.neotys.action.argument.Option.OptionalRequired.Optional;
import static com.neotys.action.argument.Option.OptionalRequired.Required;
import static com.neotys.extensions.action.ActionParameter.Type.PASSWORD;
import static com.neotys.extensions.action.ActionParameter.Type.TEXT;

enum CoapSendOption implements Option {
    CoapURL("CoapURL", Required, True, TEXT,
            "Url of the CoapRequest",
                    "Url of the CoapRequest",
                    URL_VALIDATOR),
    Method("Method", Required, True, TEXT,
            "GET, POST, PUT, DELETE,OBSERVE",
                    "method of the coap request : GET, POST, PUT, DELETE,OBSERVE",
               NON_EMPTY),
    PayLoad("PayLoad", Optional, False, TEXT,
            "Payload for  POST and PUT requests",
            "Payload for  POST and PUT requests",
            NON_EMPTY),
    FormatOfPayload("FormatOfPayload", Optional, False, TEXT,
            "APPLICATION_JSON",
            "format of the payload : TEXT_PLAIN, TEXT_HTML, TEXT_XML, IMAGE_GIF,IMAGE_JPEG,IMAGE_PNG,VIDEO_RAW,AUDIO_RAW,APPLICATION_LINK_FORMAT, APPLICATION_XML, APPLICATION_OCTET_STREAM, APPLICATION_RDF_XML,APPLICATION_SOAP_XML,APPLICATION_XMPP_XML,APPLICATION_JSON",
            NON_EMPTY),
    Accept("Accept", Optional, False, TEXT,
            "APPLICATION_JSON",
            "TEXT_PLAIN, TEXT_HTML, TEXT_XML, IMAGE_GIF,IMAGE_JPEG,IMAGE_PNG,VIDEO_RAW,AUDIO_RAW,APPLICATION_LINK_FORMAT, APPLICATION_XML, APPLICATION_OCTET_STREAM, APPLICATION_RDF_XML,APPLICATION_SOAP_XML,APPLICATION_XMPP_XML,APPLICATION_JSON",
            NON_EMPTY),
    IsAsynchronous("IsAsynchronous", Required, True, TEXT,
            "true of false to define the request is asynchronous",
            "true of false to define the request is asynchronous",
            BOOLEAN_VALIDATOR),
    TrustKeyPath("TrustKeyPath", Optional, False, TEXT,
            "Path to the Trusted keystore : cacerts",
            "Path to the Trusted keystore : cacerts",
            NON_EMPTY),
    TrustKeyPassword("TrustKeyPassword", Optional, False, PASSWORD,
            "Password of the trusted keystore",
            "Password of the trusted keystore",
            NON_EMPTY),
    KeyStoreLocation("KeyStoreLocation", Optional, False, TEXT,
            "Path to the keystore .jks file",
            "Path to the keystore file .jks",
            NON_EMPTY),
    KeyStorePassword("KeyStorePassword", Optional, False, PASSWORD,
            "Password of the keystore",
            "Password of the keystore",
            NON_EMPTY),
    KeyStoreAlias("KeyStoreAlias", Optional, False, TEXT,
            "Alias of the keystore",
            "Alias of the keystore",
            NON_EMPTY),
    KeyStoreAliasPassword("KeyStoreAliasPassword", Optional, False, PASSWORD,
            "Password of the alias",
            "Password of the alias",
            NON_EMPTY),
    TraceMode("TraceMode", Optional, False, TEXT,
            "false",
            "enable loggin details  : true: enable ; false : Disable",
              BOOLEAN_VALIDATOR);


    private final String name;
    private final Option.OptionalRequired optionalRequired;
    private final Option.AppearsByDefault appearsByDefault;
    private final ActionParameter.Type type;
    private final String defaultValue;
    private final String description;
    private final ArgumentValidator argumentValidator;

    CoapSendOption(final String name, final Option.OptionalRequired optionalRequired,
                           final Option.AppearsByDefault appearsByDefault,
                           final ActionParameter.Type type, final String defaultValue, final String description,
                           final ArgumentValidator argumentValidator)
    {
        this.name = name;
        this.optionalRequired = optionalRequired;
        this.appearsByDefault = appearsByDefault;
        this.type = type;
        this.defaultValue = defaultValue;
        this.description = description;
        this.argumentValidator = argumentValidator;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Option.OptionalRequired getOptionalRequired() {
        return optionalRequired;
    }

    @Override
    public Option.AppearsByDefault getAppearsByDefault() {
        return appearsByDefault;
    }

    @Override
    public ActionParameter.Type getType() {
        return type;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ArgumentValidator getArgumentValidator() {
        return argumentValidator;
    }

}
