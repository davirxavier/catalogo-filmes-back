--
-- PostgreSQL database dump
--

-- Dumped from database version 13.0
-- Dumped by pg_dump version 13.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (38, 'Fantasia', 'FS', 1, false);
INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (43, 'Fantasy', 'FS-EN', 2, false);
INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (39, 'Horror', 'HR', 1, false);
INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (44, 'Horror', 'HR-EN', 2, false);
INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (36, 'Comédia', 'CM', 1, false);
INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (37, 'Drama', 'DR', 1, false);
INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (42, 'Drama', 'DR-EN', 2, false);
INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (40, 'Action', 'AC-EN', 2, false);
INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (35, 'Ação', 'AC', 1, false);
INSERT INTO public.categoria (id, nome, tag, idioma, desativado) VALUES (41, 'Comedy', 'CM-EN', 2, false);


--
-- Name: categoria_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categoria_id_seq', 44, true);


--
-- PostgreSQL database dump complete
--

