module api.security.auth {

    export interface LoginResultJson {

        authenticated: boolean;

        user: api.security.UserJson;

        applications: string[];

        principals: string[];

        message?: string;
    }
}