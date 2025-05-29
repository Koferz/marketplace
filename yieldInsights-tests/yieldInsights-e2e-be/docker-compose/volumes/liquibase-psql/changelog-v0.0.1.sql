--liquibase formatted sql

--changeset skozyrev:1 labels:v0.0.1
CREATE TYPE "deposit_operation_type" AS ENUM ('close', 'prolongation');
CREATE TYPE "deposit_active_type" AS ENUM ('none', 'true', 'false');

CREATE TABLE "ads" (
	"deposit_id" text primary key constraint dep_id_length_ctr check (length("id") < 64),
	"bank_name" text constraint dep_bank_name_length_ctr check (length(bank_name) < 128),
	"deposit_name" text constraint dep_deposit_name_length_ctr check (length(deposit_name) < 128),
	"rate" text constraint dep_rate_length_ctr check (length(rate) < 12),
	"opening_date" text constraint dep_opening_date_length_ctr check (length(opening_date) < 12),
	"deposit_term" text constraint dep_deposit_term_length_ctr check (length(deposit_term) < 12),
	"deposit_amount" text constraint dep_deposit_amount_length_ctr check (length(deposit_amount) < 64),
	"deposit_operation" deposit_operation_type not null,
	"is_active" deposit_active_type not null,
	"owner_id" text not null constraint dep_owner_id_length_ctr check (length(id) < 64),
	"lock" text not null constraint ads_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX dep_owner_id_idx on "ads" using hash ("owner_id");

CREATE INDEX dep_deposit_id_idx on "ads" using hash ("deposit_id");

CREATE INDEX dep_is_active_idx on "ads" using hash ("is_active");
