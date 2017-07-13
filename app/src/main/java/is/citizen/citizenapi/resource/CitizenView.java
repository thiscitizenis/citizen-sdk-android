package is.citizen.citizenapi.resource;

/**
 * View to customise json serialization, see {@link com.fasterxml.jackson.annotation.JsonView}
 */
public interface CitizenView {

    interface User {

        /**
         * Defines fields to include in UnSecure environment
         */
        interface Register {
        }

        /**
         * Defines fields to include when user is log in
         */
        interface Login {
        }

        /**
         * Defines fields to include when getting verification data about the user.
         */
        interface Verify {
        }

        /**
         * //TODO: test this, why default inclusion is set to true
         */
        interface FixMe {

        }
    }
}
