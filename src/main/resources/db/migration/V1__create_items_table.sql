CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE public.items (
                              id UUID PRIMARY KEY,
                              name VARCHAR(255) NOT NULL,
                              credit_card VARCHAR(255),
                              currency_code VARCHAR(10)
);
